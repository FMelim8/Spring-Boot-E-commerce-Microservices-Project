import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '5s', target: 20 },
        { duration: '10s', target: 50 },
        { duration: '5s', target: 0 },
    ],
};

let JWT_TOKEN;

export function setup() {
    const url = "http://localhost:8091/api/login";

    const respToken = http.post(
        url,
        JSON.stringify({
            "username": "user2",
            "password": "user2"
        }),
        {
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
    });
    if (respToken.status === 200) {
        console.log('successful login');
        const responseJson = JSON.parse(respToken.body);
        JWT_TOKEN = "Bearer " + responseJson.access_token;
    } else {
        console.error('login failed with code: ' + respToken.status + ' ' + respToken.body);
    }
    return { JWT_TOKEN: JWT_TOKEN };
}

const BASE_URL = 'http://localhost:9095/api/order';
export default function (data) {
    let orderPayload = JSON.stringify({
        gameId: Math.floor(Math.random() * 29) + 1, //(1-29)
        quantity: Math.floor(Math.random() * 3) + 1, //(1-3)
        paymentMode: "DEBIT_CARD"
    });

    let params = {
        headers: { 
            'Content-Type': 'application/json',
            'Authorization': data.JWT_TOKEN 
        },
    };

    //Create an order
    let createOrderRes = http.post(`${BASE_URL}/placeorder`, orderPayload, params);
    
    let success = check(createOrderRes, {
        'Create order - Status 201': (res) => res.status === 201,
    });

    if (!success) {
        console.error(`Order creation failed: ${createOrderRes.status} - ${createOrderRes.body}`);
        return;
    }

    let orderData;
    try {
        orderData = createOrderRes.json();
    } catch (error) {
        console.error(`Error parsing JSON response: ${error}`);
        return;
    }

    let orderId = orderData.orderId;
    if (orderId) {
        let getOrderRes = http.get(`${BASE_URL}/${orderId}`, params);
        
        let getOrderSuccess = check(getOrderRes, {
            'Fetch order - Status 200': (res) => res.status === 200,
        });

        if (!getOrderSuccess) {
            console.error(`Order fetch failed: ${getOrderRes.status} - ${getOrderRes.body}`);
        }
    }

    sleep(1);
}

//k6 run dockerOrderRequestScript.js
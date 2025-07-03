import http from 'k6/http';
import { check, sleep } from 'k6';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

export let options = {
    stages: [
        { duration: '10s', target: 0 },
        { duration: '15s', target: 10 },
        { duration: '5', target:  15 },
        { duration: '5s', target: 5 },
        { duration: '5s', target: 150 },
        { duration: '5s', target: 40 },
        { duration: '20s', target: 10 },
        { duration: '10s', target: 0 },
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

const ORDER_BASE_URL = 'http://localhost:9095/api/order';
const GAME_BASE_URL = 'http://localhost:9094/api/games';
const KEYCLOAK_BASE_URL = 'http://localhost:8091/api';
const CallList = ["getGameById", "getAllGames", "currentUserDetails", "placeOrder", "currentUserOrders"];

const orderSortField = ["ORDERID", "GAMEID", "USERID", "ORDERDATE", "QUANTITY", "TOTALAMOUNT"];
const gameSortField = ["ID", "TITLE", "PRICE", "GENRE", "TYPE", "STOCK", "RELEASEDATE"];
const direction = ["ASC", "DESC"];

export default function (data) {

    let params = {
        headers: { 
            'Content-Type': 'application/json',
            'Authorization': data.JWT_TOKEN 
        },
    };

    const call = CallList[Math.floor(Math.random() * CallList.length)]

    switch (call){
        case "placeOrder":

            let payload = JSON.stringify({
                gameId: Math.floor(Math.random() * 29) + 1, //(1-29)
                quantity: Math.floor(Math.random() * 3) + 1, //(1-3)
                paymentMode: "DEBIT_CARD"
            });
            
            let createOrderRes = http.post(`${ORDER_BASE_URL}/placeorder`, payload, params);
            
            var success = check(createOrderRes, {
                'Create order - Status 201': (res) => res.status === 201,
            });

            if (!success) {
                console.error(`Order creation failed: ${createOrderRes.status} - ${createOrderRes.body}`);
                return;
            }

            break;
        
        case "currentUserOrders":
            let ci = Math.floor(Math.random() * 28) + 1;
            let cl = Math.floor(Math.random() * 6);
            let ct = Math.floor(Math.random() * 2);
            
            let cquery = "?page=" + ci + "&sortField=" + orderSortField[cl] + "&direction=" + direction[ct];

            let getOrderRes = http.get(`${ORDER_BASE_URL}/currentUser${cquery}`, params);
                
            var success = check(getOrderRes, {
                'Get order Page - Status 200': (res) => res.status === 200,
            });
            
            if (!success) {
                console.error(`Get order Page failed: ${getOrderRes.status} - ${getOrderRes.body}`);
                return;
            }

            break;
        
        case "currentUserDetails":
            let getCurrentUserDetailsRes = http.get(`${KEYCLOAK_BASE_URL}/userDetails`, params);

            var success = check(getCurrentUserDetailsRes, {
                'Get Current User Details - Status 200': (res) => res.status === 200,
            })

            if (!success) {
                console.error(`Get Current User Details failed: ${getCurrentUserDetailsRes.status} - ${getCurrentUserDetailsRes.body}`);
                return;
            }

            break;

        case "getAllGames":
            let gi = Math.floor(Math.random() * 3) + 1;
            let gl = Math.floor(Math.random() * 6);
            let gt = Math.floor(Math.random() * 2);
            
            let gquery = "?page=" + gi + "&sortField=" + gameSortField[gl] + "&direction=" + direction[gt];

            let getGamesRes = http.get(`${GAME_BASE_URL}/all${gquery}`, params);
                
            var success = check(getGamesRes, {
                'Get game Page - Status 200': (res) => res.status === 200,
            });
            
            if (!success) {
                console.error(`Get game Page failed: ${getGamesRes.status} - ${getGamesRes.body}`);
                return;
            }

            break;
        case "getGameById":

            let id = Math.floor(Math.random() * 29) + 1;
            
            let getGameByIdRes = http.get(`${GAME_BASE_URL}/${id}`, params);

            var success = check(getGameByIdRes, {
                'Get game by id - Status 200': (res) => res.status === 200,
            })

            if (!success) {
                console.error(`Get game by id failed: ${getGameByIdRes.status} - ${getGameByIdRes.body}`);
                return;
            }

            break;
    }
    sleep(1);
}

// export function handleSummary(data) {
//     return {
//       "getOrdersSummary.html": htmlReport(data),
//     };
//   }

// word wrap shortcut -> alt + z
//k6 run test.js
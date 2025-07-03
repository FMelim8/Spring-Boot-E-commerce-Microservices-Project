{{/*
Keycloak realm import file.
*/}}
{{- define "services.keycloakImportFile" }}
microservices-realm.json: |    
    {
      "id" : "f7b0a19c-1e66-4251-9d18-5a91ec8e0ab2",
      "realm" : "microservices-realm",
      "notBefore" : 0,
      "defaultSignatureAlgorithm" : "RS256",
      "revokeRefreshToken" : false,
      "refreshTokenMaxReuse" : 0,
      "accessTokenLifespan" : 600,
      "accessTokenLifespanForImplicitFlow" : 900,
      "ssoSessionIdleTimeout" : 1800,
      "ssoSessionMaxLifespan" : 36000,
      "ssoSessionIdleTimeoutRememberMe" : 0,
      "ssoSessionMaxLifespanRememberMe" : 0,
      "offlineSessionIdleTimeout" : 2592000,
      "offlineSessionMaxLifespanEnabled" : false,
      "offlineSessionMaxLifespan" : 5184000,
      "clientSessionIdleTimeout" : 0,
      "clientSessionMaxLifespan" : 0,
      "clientOfflineSessionIdleTimeout" : 0,
      "clientOfflineSessionMaxLifespan" : 0,
      "accessCodeLifespan" : 60,
      "accessCodeLifespanUserAction" : 900,
      "accessCodeLifespanLogin" : 1800,
      "actionTokenGeneratedByAdminLifespan" : 43200,
      "actionTokenGeneratedByUserLifespan" : 600,
      "oauth2DeviceCodeLifespan" : 1500,
      "oauth2DevicePollingInterval" : 10,
      "enabled" : true,
      "sslRequired" : "external",
      "registrationAllowed" : true,
      "registrationEmailAsUsername" : false,
      "rememberMe" : false,
      "verifyEmail" : false,
      "loginWithEmailAllowed" : true,
      "duplicateEmailsAllowed" : false,
      "resetPasswordAllowed" : false,
      "editUsernameAllowed" : false,
      "bruteForceProtected" : false,
      "permanentLockout" : false,
      "maxFailureWaitSeconds" : 900,
      "minimumQuickLoginWaitSeconds" : 60,
      "waitIncrementSeconds" : 60,
      "quickLoginCheckMilliSeconds" : 1000,
      "maxDeltaTimeSeconds" : 43200,
      "failureFactor" : 30,
      "roles" : {
        "realm" : [ {
          "id" : "2b5852cd-8f45-4fbe-a507-ecfbfe1167a2",
          "name" : "app_admin",
          "description" : "",
          "composite" : true,
          "composites" : {
            "realm" : [ "uma_authorization", "app_admin" ],
            "client" : {
              "keycloak-client" : [ "admin" ],
              "broker" : [ "read-token" ],
              "account" : [ "delete-account", "view-consent", "manage-account", "manage-account-links", "view-profile", "manage-consent", "view-groups", "view-applications" ]
            }
          },
          "clientRole" : false,
          "containerId" : "f7b0a19c-1e66-4251-9d18-5a91ec8e0ab2",
          "attributes" : { }
        }, {
          "id" : "3a406660-b689-4227-a870-ad5709a74d79",
          "name" : "uma_authorization",
          "description" : "${role_uma_authorization}",
          "composite" : false,
          "clientRole" : false,
          "containerId" : "f7b0a19c-1e66-4251-9d18-5a91ec8e0ab2",
          "attributes" : { }
        }, {
          "id" : "65725e87-3eb5-47a9-bc2d-dd1f9e63bfc5",
          "name" : "app_user",
          "description" : "",
          "composite" : true,
          "composites" : {
            "client" : {
              "keycloak-client" : [ "user" ]
            }
          },
          "clientRole" : false,
          "containerId" : "f7b0a19c-1e66-4251-9d18-5a91ec8e0ab2",
          "attributes" : { }
        }, {
          "id" : "6f4f99b9-ef75-43f5-bcc2-6b11fa6a2454",
          "name" : "default-roles-microservices-realm",
          "description" : "${role_default-roles}",
          "composite" : true,
          "composites" : {
            "client" : {
              "keycloak-client" : [ "user" ],
              "account" : [ "manage-account" ]
            }
          },
          "clientRole" : false,
          "containerId" : "f7b0a19c-1e66-4251-9d18-5a91ec8e0ab2",
          "attributes" : { }
        }, {
          "id" : "d2e4919f-0578-4469-9d85-8a82ac9e740c",
          "name" : "app_service",
          "description" : "",
          "composite" : true,
          "composites" : {
            "client" : {
              "keycloak-client" : [ "service" ],
              "broker" : [ "read-token" ]
            }
          },
          "clientRole" : false,
          "containerId" : "f7b0a19c-1e66-4251-9d18-5a91ec8e0ab2",
          "attributes" : { }
        }, {
          "id" : "696bc30e-a073-44be-8fc9-125107ab1cfd",
          "name" : "offline_access",
          "description" : "${role_offline-access}",
          "composite" : false,
          "clientRole" : false,
          "containerId" : "f7b0a19c-1e66-4251-9d18-5a91ec8e0ab2",
          "attributes" : { }
        } ],
        "client" : {
          "realm-management" : [ {
            "id" : "a2987ac7-f674-4ad7-b96e-ffbde40aaa5d",
            "name" : "query-users",
            "description" : "${role_query-users}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "99f818b0-57f7-43ec-ba35-7f287742abaa",
            "name" : "query-clients",
            "description" : "${role_query-clients}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "086a5c54-c3c9-4415-a20e-9b1048902ead",
            "name" : "create-client",
            "description" : "${role_create-client}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "1b53dd53-0d4d-456e-8c94-c1ac3d686b88",
            "name" : "query-groups",
            "description" : "${role_query-groups}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "b3858eb9-0a90-49b9-be4f-4f5469fec329",
            "name" : "view-authorization",
            "description" : "${role_view-authorization}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "1ec14a29-de5b-480e-b817-967a31eca59e",
            "name" : "manage-realm",
            "description" : "${role_manage-realm}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "7cfb85c9-fd1d-420e-b1d7-1bb84a1fc0b0",
            "name" : "view-realm",
            "description" : "${role_view-realm}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "aa4b69b1-6688-4cf0-95e2-889d4e54a844",
            "name" : "manage-events",
            "description" : "${role_manage-events}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "28fca6a3-a308-4ebf-b126-a13d68cb6fc8",
            "name" : "manage-identity-providers",
            "description" : "${role_manage-identity-providers}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "ea8249cd-3fc1-4412-a828-30e5d634c778",
            "name" : "realm-admin",
            "description" : "${role_realm-admin}",
            "composite" : true,
            "composites" : {
              "client" : {
                "realm-management" : [ "query-users", "query-clients", "create-client", "query-groups", "view-authorization", "manage-realm", "view-realm", "manage-events", "manage-identity-providers", "manage-clients", "impersonation", "view-users", "view-identity-providers", "manage-authorization", "manage-users", "query-realms", "view-events", "view-clients" ]
              }
            },
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "6956d52c-1fe9-47ed-8560-27fe94e43f84",
            "name" : "impersonation",
            "description" : "${role_impersonation}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "618e773e-d357-4b3c-81b1-111272a02dee",
            "name" : "manage-clients",
            "description" : "${role_manage-clients}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "ef64fe38-bcb0-49fa-bdc3-1447cf2ef604",
            "name" : "view-users",
            "description" : "${role_view-users}",
            "composite" : true,
            "composites" : {
              "client" : {
                "realm-management" : [ "query-users", "query-groups" ]
              }
            },
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "96452901-292b-4a40-b8e6-63a153630295",
            "name" : "view-identity-providers",
            "description" : "${role_view-identity-providers}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "fccb67ad-2f7c-46b7-83d1-08c0077a549c",
            "name" : "manage-authorization",
            "description" : "${role_manage-authorization}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "7c0a628f-be87-42cb-aa2d-fff7ea4374c7",
            "name" : "manage-users",
            "description" : "${role_manage-users}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "c15b2610-851f-4a14-88bd-40ed8eaf50a2",
            "name" : "query-realms",
            "description" : "${role_query-realms}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "568a0f17-3f67-425c-9262-7c72d229b5b6",
            "name" : "view-events",
            "description" : "${role_view-events}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          }, {
            "id" : "ba52db5c-41d2-4aad-bd8b-ce02c8d49b79",
            "name" : "view-clients",
            "description" : "${role_view-clients}",
            "composite" : true,
            "composites" : {
              "client" : {
                "realm-management" : [ "query-clients" ]
              }
            },
            "clientRole" : true,
            "containerId" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
            "attributes" : { }
          } ],
          "keycloak-client" : [ {
            "id" : "46293fa7-96e8-4c4c-a555-ba37a6239c98",
            "name" : "service",
            "description" : "",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "495086f6-ae80-470c-8c6a-eaa1a4cb359e",
            "attributes" : { }
          }, {
            "id" : "6f4fd4a9-091c-45ab-ad7e-c5712f23d47c",
            "name" : "user",
            "description" : "",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "495086f6-ae80-470c-8c6a-eaa1a4cb359e",
            "attributes" : { }
          }, {
            "id" : "09dc1779-ff57-4e66-a00e-013f49c9beff",
            "name" : "admin",
            "description" : "",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "495086f6-ae80-470c-8c6a-eaa1a4cb359e",
            "attributes" : { }
          } ],
          "security-admin-console" : [ ],
          "admin-cli" : [ {
            "id" : "decb344a-68d9-43ed-b8e8-7f22badee4e1",
            "name" : "admin-role",
            "description" : "",
            "composite" : true,
            "composites" : {
              "client" : {
                "account" : [ "manage-account", "manage-account-links" ]
              }
            },
            "clientRole" : true,
            "containerId" : "676b315a-0988-4fed-ad93-6220d66f071e",
            "attributes" : { }
          }, {
            "id" : "221b6bad-cb31-4532-959e-37087c409f5b",
            "name" : "uma_protection",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "676b315a-0988-4fed-ad93-6220d66f071e",
            "attributes" : { }
          } ],
          "account-console" : [ ],
          "broker" : [ {
            "id" : "08a8c33c-4682-4965-8163-7df640c02873",
            "name" : "read-token",
            "description" : "${role_read-token}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "277b348d-b338-400c-9018-d4f1d2af346b",
            "attributes" : { }
          } ],
          "account" : [ {
            "id" : "caa3cb87-8195-473b-9a07-e549858d3c9b",
            "name" : "delete-account",
            "description" : "${role_delete-account}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "16f56daf-8551-46a4-b7c8-bedfaef67641",
            "attributes" : { }
          }, {
            "id" : "2f3c0543-579e-4f48-8ba1-96b8affb18f4",
            "name" : "view-consent",
            "description" : "${role_view-consent}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "16f56daf-8551-46a4-b7c8-bedfaef67641",
            "attributes" : { }
          }, {
            "id" : "9e593d2e-c08a-437a-b773-5ea20c11e3a1",
            "name" : "manage-account",
            "description" : "${role_manage-account}",
            "composite" : true,
            "composites" : {
              "client" : {
                "account" : [ "manage-account-links" ]
              }
            },
            "clientRole" : true,
            "containerId" : "16f56daf-8551-46a4-b7c8-bedfaef67641",
            "attributes" : { }
          }, {
            "id" : "850ca236-0692-4ae0-a7c9-49d6447f0565",
            "name" : "manage-account-links",
            "description" : "${role_manage-account-links}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "16f56daf-8551-46a4-b7c8-bedfaef67641",
            "attributes" : { }
          }, {
            "id" : "dfafa765-031d-4cfb-868f-14314b456361",
            "name" : "view-profile",
            "description" : "${role_view-profile}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "16f56daf-8551-46a4-b7c8-bedfaef67641",
            "attributes" : { }
          }, {
            "id" : "ee85c4d3-5a04-4190-8071-6b8e323f4f20",
            "name" : "manage-consent",
            "description" : "${role_manage-consent}",
            "composite" : true,
            "composites" : {
              "client" : {
                "account" : [ "view-consent" ]
              }
            },
            "clientRole" : true,
            "containerId" : "16f56daf-8551-46a4-b7c8-bedfaef67641",
            "attributes" : { }
          }, {
            "id" : "ec62552e-55cc-43e2-864c-217ae49450a0",
            "name" : "view-groups",
            "description" : "${role_view-groups}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "16f56daf-8551-46a4-b7c8-bedfaef67641",
            "attributes" : { }
          }, {
            "id" : "ae116667-8d07-49b4-991f-e3f3a3b7cbef",
            "name" : "view-applications",
            "description" : "${role_view-applications}",
            "composite" : false,
            "clientRole" : true,
            "containerId" : "16f56daf-8551-46a4-b7c8-bedfaef67641",
            "attributes" : { }
          } ]
        }
      },
      "groups" : [ ],
      "defaultRole" : {
        "id" : "6f4f99b9-ef75-43f5-bcc2-6b11fa6a2454",
        "name" : "default-roles-microservices-realm",
        "description" : "${role_default-roles}",
        "composite" : true,
        "clientRole" : false,
        "containerId" : "f7b0a19c-1e66-4251-9d18-5a91ec8e0ab2"
      },
      "requiredCredentials" : [ "password" ],
      "otpPolicyType" : "totp",
      "otpPolicyAlgorithm" : "HmacSHA1",
      "otpPolicyInitialCounter" : 0,
      "otpPolicyDigits" : 6,
      "otpPolicyLookAheadWindow" : 1,
      "otpPolicyPeriod" : 30,
      "otpPolicyCodeReusable" : false,
      "otpSupportedApplications" : [ "totpAppFreeOTPName", "totpAppGoogleName", "totpAppMicrosoftAuthenticatorName" ],
      "localizationTexts" : { },
      "webAuthnPolicyRpEntityName" : "keycloak",
      "webAuthnPolicySignatureAlgorithms" : [ "ES256" ],
      "webAuthnPolicyRpId" : "",
      "webAuthnPolicyAttestationConveyancePreference" : "not specified",
      "webAuthnPolicyAuthenticatorAttachment" : "not specified",
      "webAuthnPolicyRequireResidentKey" : "not specified",
      "webAuthnPolicyUserVerificationRequirement" : "not specified",
      "webAuthnPolicyCreateTimeout" : 0,
      "webAuthnPolicyAvoidSameAuthenticatorRegister" : false,
      "webAuthnPolicyAcceptableAaguids" : [ ],
      "webAuthnPolicyExtraOrigins" : [ ],
      "webAuthnPolicyPasswordlessRpEntityName" : "keycloak",
      "webAuthnPolicyPasswordlessSignatureAlgorithms" : [ "ES256" ],
      "webAuthnPolicyPasswordlessRpId" : "",
      "webAuthnPolicyPasswordlessAttestationConveyancePreference" : "not specified",
      "webAuthnPolicyPasswordlessAuthenticatorAttachment" : "not specified",
      "webAuthnPolicyPasswordlessRequireResidentKey" : "not specified",
      "webAuthnPolicyPasswordlessUserVerificationRequirement" : "not specified",
      "webAuthnPolicyPasswordlessCreateTimeout" : 0,
      "webAuthnPolicyPasswordlessAvoidSameAuthenticatorRegister" : false,
      "webAuthnPolicyPasswordlessAcceptableAaguids" : [ ],
      "webAuthnPolicyPasswordlessExtraOrigins" : [ ],
      "users" : [ {
        "id" : "3bf423b4-f0bc-4edc-a207-01509c055e26",
        "createdTimestamp" : 1741018561016,
        "username" : "service-account-admin-cli",
        "enabled" : true,
        "totp" : false,
        "emailVerified" : false,
        "serviceAccountClientId" : "admin-cli",
        "credentials" : [ ],
        "disableableCredentialTypes" : [ ],
        "requiredActions" : [ ],
        "realmRoles" : [ "default-roles-microservices-realm", "app_service" ],
        "clientRoles" : {
          "realm-management" : [ "manage-users" ],
          "keycloak-client" : [ "service" ],
          "admin-cli" : [ "uma_protection" ]
        },
        "notBefore" : 0,
        "groups" : [ ]
      }, {
        "id" : "dec92c4b-79ba-4c1d-99bb-6eb8aee63241",
        "createdTimestamp" : 1740667496313,
        "username" : "service-account-keycloak-client",
        "enabled" : true,
        "totp" : false,
        "emailVerified" : false,
        "serviceAccountClientId" : "keycloak-client",
        "credentials" : [ ],
        "disableableCredentialTypes" : [ ],
        "requiredActions" : [ ],
        "realmRoles" : [ "default-roles-microservices-realm", "app_service" ],
        "clientRoles" : {
          "account" : [ "manage-account" ]
        },
        "notBefore" : 0,
        "groups" : [ ]
      }, {
        "id" : "2a117809-8285-4718-a131-bd61fb57cd1f",
        "createdTimestamp" : 1740654221121,
        "username" : "user1",
        "enabled" : true,
        "totp" : false,
        "emailVerified" : true,
        "email" : "user1@email.com",
        "credentials" : [ {
          "id" : "4e67ce9c-1ddc-45db-b36e-db50ee1f4d9e",
          "type" : "password",
          "userLabel" : "My password",
          "createdDate" : 1740654903754,
          "secretData" : "{\"value\":\"9qur72LAICdhFeb2SpkJt2jbOYaxJ44g9T+E7bmJXxI=\",\"salt\":\"AGHjXoWftkFxnk/dcZRNnA==\",\"additionalParameters\":{}}",
          "credentialData" : "{\"hashIterations\":27500,\"algorithm\":\"pbkdf2-sha256\",\"additionalParameters\":{}}"
        } ],
        "disableableCredentialTypes" : [ ],
        "requiredActions" : [ ],
        "realmRoles" : [ "app_admin", "default-roles-microservices-realm" ],
        "clientRoles" : {
          "admin-cli" : [ "admin-role" ]
        },
        "notBefore" : 0,
        "groups" : [ ]
      }, {
        "id" : "4142fff7-35d4-4467-8434-346fbac50374",
        "createdTimestamp" : 1740654292335,
        "username" : "user2",
        "enabled" : true,
        "totp" : false,
        "emailVerified" : true,
        "email" : "user2@email.com",
        "credentials" : [ {
          "id" : "7c4b99d0-4521-4b65-850f-69d763276743",
          "type" : "password",
          "userLabel" : "My password",
          "createdDate" : 1740654308208,
          "secretData" : "{\"value\":\"6DxWP90CQpqfKId867L/6TDW87OaLKhqNlf4qP1xfiE=\",\"salt\":\"YSjYfyBH4a5AR5CSi61DxQ==\",\"additionalParameters\":{}}",
          "credentialData" : "{\"hashIterations\":27500,\"algorithm\":\"pbkdf2-sha256\",\"additionalParameters\":{}}"
        } ],
        "disableableCredentialTypes" : [ ],
        "requiredActions" : [ ],
        "realmRoles" : [ "default-roles-microservices-realm", "app_user" ],
        "notBefore" : 0,
        "groups" : [ ]
      } ],
      "scopeMappings" : [ {
        "clientScope" : "offline_access",
        "roles" : [ "offline_access" ]
      } ],
      "clientScopeMappings" : {
        "account" : [ {
          "client" : "account-console",
          "roles" : [ "manage-account", "view-groups" ]
        } ]
      },
      "clients" : [ {
        "id" : "16f56daf-8551-46a4-b7c8-bedfaef67641",
        "clientId" : "account",
        "name" : "${client_account}",
        "rootUrl" : "${authBaseUrl}",
        "baseUrl" : "/realms/microservices-realm/account/",
        "surrogateAuthRequired" : false,
        "enabled" : true,
        "alwaysDisplayInConsole" : false,
        "clientAuthenticatorType" : "client-secret",
        "redirectUris" : [ "/realms/microservices-realm/account/*" ],
        "webOrigins" : [ ],
        "notBefore" : 0,
        "bearerOnly" : false,
        "consentRequired" : false,
        "standardFlowEnabled" : true,
        "implicitFlowEnabled" : false,
        "directAccessGrantsEnabled" : false,
        "serviceAccountsEnabled" : false,
        "publicClient" : true,
        "frontchannelLogout" : false,
        "protocol" : "openid-connect",
        "attributes" : {
          "post.logout.redirect.uris" : "+"
        },
        "authenticationFlowBindingOverrides" : { },
        "fullScopeAllowed" : false,
        "nodeReRegistrationTimeout" : 0,
        "defaultClientScopes" : [ "web-origins", "acr", "roles", "profile", "email" ],
        "optionalClientScopes" : [ "address", "phone", "offline_access", "microprofile-jwt" ]
      }, {
        "id" : "be4b3f4b-94bc-4650-8b50-4bfbaacb062a",
        "clientId" : "account-console",
        "name" : "${client_account-console}",
        "rootUrl" : "${authBaseUrl}",
        "baseUrl" : "/realms/microservices-realm/account/",
        "surrogateAuthRequired" : false,
        "enabled" : true,
        "alwaysDisplayInConsole" : false,
        "clientAuthenticatorType" : "client-secret",
        "redirectUris" : [ "/realms/microservices-realm/account/*" ],
        "webOrigins" : [ ],
        "notBefore" : 0,
        "bearerOnly" : false,
        "consentRequired" : false,
        "standardFlowEnabled" : true,
        "implicitFlowEnabled" : false,
        "directAccessGrantsEnabled" : false,
        "serviceAccountsEnabled" : false,
        "publicClient" : true,
        "frontchannelLogout" : false,
        "protocol" : "openid-connect",
        "attributes" : {
          "post.logout.redirect.uris" : "+",
          "pkce.code.challenge.method" : "S256"
        },
        "authenticationFlowBindingOverrides" : { },
        "fullScopeAllowed" : false,
        "nodeReRegistrationTimeout" : 0,
        "protocolMappers" : [ {
          "id" : "7d77cc94-0296-4956-9d2f-5b13198b351d",
          "name" : "audience resolve",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-audience-resolve-mapper",
          "consentRequired" : false,
          "config" : { }
        } ],
        "defaultClientScopes" : [ "web-origins", "acr", "roles", "profile", "email" ],
        "optionalClientScopes" : [ "address", "phone", "offline_access", "microprofile-jwt" ]
      }, {
        "id" : "676b315a-0988-4fed-ad93-6220d66f071e",
        "clientId" : "admin-cli",
        "name" : "${client_admin-cli}",
        "description" : "",
        "rootUrl" : "",
        "adminUrl" : "",
        "baseUrl" : "",
        "surrogateAuthRequired" : false,
        "enabled" : true,
        "alwaysDisplayInConsole" : false,
        "clientAuthenticatorType" : "client-secret",
        "secret" : "MK6z3NJCRT9oTRqXXPvaKnQmm6D0UMbB",
        "redirectUris" : [ ],
        "webOrigins" : [ ],
        "notBefore" : 0,
        "bearerOnly" : false,
        "consentRequired" : false,
        "standardFlowEnabled" : false,
        "implicitFlowEnabled" : false,
        "directAccessGrantsEnabled" : true,
        "serviceAccountsEnabled" : true,
        "authorizationServicesEnabled" : true,
        "publicClient" : false,
        "frontchannelLogout" : false,
        "protocol" : "openid-connect",
        "attributes" : {
          "oidc.ciba.grant.enabled" : "false",
          "client.secret.creation.time" : "1741018561",
          "backchannel.logout.session.required" : "true",
          "oauth2.device.authorization.grant.enabled" : "false",
          "display.on.consent.screen" : "false",
          "backchannel.logout.revoke.offline.tokens" : "false"
        },
        "authenticationFlowBindingOverrides" : { },
        "fullScopeAllowed" : false,
        "nodeReRegistrationTimeout" : 0,
        "protocolMappers" : [ {
          "id" : "2194838b-3ce7-467f-a08a-00502ab651a5",
          "name" : "Client Host",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usersessionmodel-note-mapper",
          "consentRequired" : false,
          "config" : {
            "user.session.note" : "clientHost",
            "introspection.token.claim" : "true",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "clientHost",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "a619aa61-9a16-441f-9858-9e8b80a9caae",
          "name" : "Client ID",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usersessionmodel-note-mapper",
          "consentRequired" : false,
          "config" : {
            "user.session.note" : "client_id",
            "introspection.token.claim" : "true",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "client_id",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "5a9dd43c-284b-460d-8e6d-28e3c2173c3c",
          "name" : "Client IP Address",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usersessionmodel-note-mapper",
          "consentRequired" : false,
          "config" : {
            "user.session.note" : "clientAddress",
            "introspection.token.claim" : "true",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "clientAddress",
            "jsonType.label" : "String"
          }
        } ],
        "defaultClientScopes" : [ "web-origins", "acr", "roles", "profile", "email" ],
        "optionalClientScopes" : [ "address", "phone", "offline_access", "microprofile-jwt" ]
      }, {
        "id" : "277b348d-b338-400c-9018-d4f1d2af346b",
        "clientId" : "broker",
        "name" : "${client_broker}",
        "surrogateAuthRequired" : false,
        "enabled" : true,
        "alwaysDisplayInConsole" : false,
        "clientAuthenticatorType" : "client-secret",
        "redirectUris" : [ ],
        "webOrigins" : [ ],
        "notBefore" : 0,
        "bearerOnly" : true,
        "consentRequired" : false,
        "standardFlowEnabled" : true,
        "implicitFlowEnabled" : false,
        "directAccessGrantsEnabled" : false,
        "serviceAccountsEnabled" : false,
        "publicClient" : false,
        "frontchannelLogout" : false,
        "protocol" : "openid-connect",
        "attributes" : { },
        "authenticationFlowBindingOverrides" : { },
        "fullScopeAllowed" : false,
        "nodeReRegistrationTimeout" : 0,
        "defaultClientScopes" : [ "web-origins", "acr", "roles", "profile", "email" ],
        "optionalClientScopes" : [ "address", "phone", "offline_access", "microprofile-jwt" ]
      }, {
        "id" : "495086f6-ae80-470c-8c6a-eaa1a4cb359e",
        "clientId" : "keycloak-client",
        "name" : "",
        "description" : "",
        "rootUrl" : "",
        "adminUrl" : "",
        "baseUrl" : "",
        "surrogateAuthRequired" : false,
        "enabled" : true,
        "alwaysDisplayInConsole" : false,
        "clientAuthenticatorType" : "client-secret",
        "secret" : "KdzCgauJ2QR1rRGUmAQsBOeMAyCi96kP",
        "redirectUris" : [ "http://localhost:9091/*" ],
        "webOrigins" : [ "/*" ],
        "notBefore" : 0,
        "bearerOnly" : false,
        "consentRequired" : false,
        "standardFlowEnabled" : false,
        "implicitFlowEnabled" : false,
        "directAccessGrantsEnabled" : true,
        "serviceAccountsEnabled" : true,
        "publicClient" : false,
        "frontchannelLogout" : true,
        "protocol" : "openid-connect",
        "attributes" : {
          "oidc.ciba.grant.enabled" : "false",
          "client.secret.creation.time" : "1740653684",
          "backchannel.logout.session.required" : "true",
          "oauth2.device.authorization.grant.enabled" : "false",
          "display.on.consent.screen" : "false",
          "backchannel.logout.revoke.offline.tokens" : "false"
        },
        "authenticationFlowBindingOverrides" : { },
        "fullScopeAllowed" : true,
        "nodeReRegistrationTimeout" : -1,
        "protocolMappers" : [ {
          "id" : "dfa14e29-a79b-4b5c-a511-83d2d31bb302",
          "name" : "Custom Audience Mapper",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-audience-mapper",
          "consentRequired" : false,
          "config" : {
            "id.token.claim" : "false",
            "access.token.claim" : "true",
            "introspection.token.claim" : "true",
            "included.custom.audience" : "backend-api"
          }
        }, {
          "id" : "180f5237-7874-40aa-b41f-36206c145373",
          "name" : "Client Host",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usersessionmodel-note-mapper",
          "consentRequired" : false,
          "config" : {
            "user.session.note" : "clientHost",
            "introspection.token.claim" : "true",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "clientHost",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "f02e9f8f-531c-4edf-a080-0c3cccc4646d",
          "name" : "Client ID",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usersessionmodel-note-mapper",
          "consentRequired" : false,
          "config" : {
            "user.session.note" : "client_id",
            "introspection.token.claim" : "true",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "client_id",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "925bd4f4-c841-40bc-b2f2-51519bbd3db4",
          "name" : "Client IP Address",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usersessionmodel-note-mapper",
          "consentRequired" : false,
          "config" : {
            "user.session.note" : "clientAddress",
            "introspection.token.claim" : "true",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "clientAddress",
            "jsonType.label" : "String"
          }
        } ],
        "defaultClientScopes" : [ "web-origins", "acr", "openid", "roles", "profile", "email" ],
        "optionalClientScopes" : [ "address", "phone", "offline_access", "microprofile-jwt" ]
      }, {
        "id" : "9c4bd282-22fc-4234-9262-5bc06654d5f9",
        "clientId" : "realm-management",
        "name" : "${client_realm-management}",
        "surrogateAuthRequired" : false,
        "enabled" : true,
        "alwaysDisplayInConsole" : false,
        "clientAuthenticatorType" : "client-secret",
        "redirectUris" : [ ],
        "webOrigins" : [ ],
        "notBefore" : 0,
        "bearerOnly" : true,
        "consentRequired" : false,
        "standardFlowEnabled" : true,
        "implicitFlowEnabled" : false,
        "directAccessGrantsEnabled" : false,
        "serviceAccountsEnabled" : false,
        "publicClient" : false,
        "frontchannelLogout" : false,
        "protocol" : "openid-connect",
        "attributes" : { },
        "authenticationFlowBindingOverrides" : { },
        "fullScopeAllowed" : false,
        "nodeReRegistrationTimeout" : 0,
        "defaultClientScopes" : [ "web-origins", "acr", "roles", "profile", "email" ],
        "optionalClientScopes" : [ "address", "phone", "offline_access", "microprofile-jwt" ]
      }, {
        "id" : "81c3e004-3eba-416a-afcb-ab20327afdba",
        "clientId" : "security-admin-console",
        "name" : "${client_security-admin-console}",
        "rootUrl" : "${authAdminUrl}",
        "baseUrl" : "/admin/microservices-realm/console/",
        "surrogateAuthRequired" : false,
        "enabled" : true,
        "alwaysDisplayInConsole" : false,
        "clientAuthenticatorType" : "client-secret",
        "redirectUris" : [ "/admin/microservices-realm/console/*" ],
        "webOrigins" : [ "+" ],
        "notBefore" : 0,
        "bearerOnly" : false,
        "consentRequired" : false,
        "standardFlowEnabled" : true,
        "implicitFlowEnabled" : false,
        "directAccessGrantsEnabled" : false,
        "serviceAccountsEnabled" : false,
        "publicClient" : true,
        "frontchannelLogout" : false,
        "protocol" : "openid-connect",
        "attributes" : {
          "post.logout.redirect.uris" : "+",
          "pkce.code.challenge.method" : "S256"
        },
        "authenticationFlowBindingOverrides" : { },
        "fullScopeAllowed" : false,
        "nodeReRegistrationTimeout" : 0,
        "protocolMappers" : [ {
          "id" : "b2ae6222-8c41-434d-ac4e-b72537551067",
          "name" : "locale",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "locale",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "locale",
            "jsonType.label" : "String"
          }
        } ],
        "defaultClientScopes" : [ "web-origins", "acr", "roles", "profile", "email" ],
        "optionalClientScopes" : [ "address", "phone", "offline_access", "microprofile-jwt" ]
      } ],
      "clientScopes" : [ {
        "id" : "59626dcc-a4ec-4fcf-9f7a-787f3a014a1e",
        "name" : "roles",
        "description" : "OpenID Connect scope for add user roles to the access token",
        "protocol" : "openid-connect",
        "attributes" : {
          "include.in.token.scope" : "false",
          "display.on.consent.screen" : "true",
          "consent.screen.text" : "${rolesScopeConsentText}"
        },
        "protocolMappers" : [ {
          "id" : "d92fdfd1-b7fc-46be-a2cc-554fdf8da12d",
          "name" : "client roles",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-client-role-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "multivalued" : "true",
            "userinfo.token.claim" : "false",
            "user.attribute" : "foo",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "resource_access.${client_id}.roles",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "5144bd1a-d019-47c4-8364-db938847329d",
          "name" : "realm roles",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-realm-role-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "multivalued" : "true",
            "user.attribute" : "foo",
            "access.token.claim" : "true",
            "claim.name" : "realm_access.roles",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "323d9c8d-51b6-4a08-bc78-f8271bbbfc45",
          "name" : "audience resolve",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-audience-resolve-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "access.token.claim" : "true"
          }
        } ]
      }, {
        "id" : "336a4161-fdd5-42f1-8f56-b7a66b19adb7",
        "name" : "profile",
        "description" : "OpenID Connect built-in scope: profile",
        "protocol" : "openid-connect",
        "attributes" : {
          "include.in.token.scope" : "true",
          "display.on.consent.screen" : "true",
          "consent.screen.text" : "${profileScopeConsentText}"
        },
        "protocolMappers" : [ {
          "id" : "acf9362d-7ed4-4f03-88fc-c536ea591058",
          "name" : "birthdate",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "birthdate",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "birthdate",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "9b7fecf5-d6df-45b6-a1d1-1d4ccee95a1a",
          "name" : "website",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "website",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "website",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "8da96c2e-eac1-4762-8a63-ba82a0d9febe",
          "name" : "updated at",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "updatedAt",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "updated_at",
            "jsonType.label" : "long"
          }
        }, {
          "id" : "1c3f1a62-5c6e-4053-8dc6-0e5a5933606c",
          "name" : "given name",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "firstName",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "given_name",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "1b16bc35-47d0-40fe-b719-4436b86229a5",
          "name" : "locale",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "locale",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "locale",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "9cf26de4-40cc-4611-af53-dac8495bc095",
          "name" : "gender",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "gender",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "gender",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "ddd22956-0409-46ad-8e08-ae93272be5db",
          "name" : "zoneinfo",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "zoneinfo",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "zoneinfo",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "f2957aa6-db51-439b-8b85-a1c0acca32e1",
          "name" : "family name",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "lastName",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "family_name",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "2d5d2e60-589b-4727-8e2c-f18667aa9138",
          "name" : "username",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "username",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "preferred_username",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "cbd66351-f084-45f4-b4b3-168e3b5eaf00",
          "name" : "full name",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-full-name-mapper",
          "consentRequired" : false,
          "config" : {
            "id.token.claim" : "true",
            "introspection.token.claim" : "true",
            "access.token.claim" : "true",
            "userinfo.token.claim" : "true"
          }
        }, {
          "id" : "0ca7b1fa-eda5-459d-a53b-ddb9949a1635",
          "name" : "nickname",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "nickname",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "nickname",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "0ee78cdc-39f4-4954-acae-2933e08bf3e4",
          "name" : "profile",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "profile",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "profile",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "fa79030d-2cef-4af7-8a57-d2524aae611e",
          "name" : "middle name",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "middleName",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "middle_name",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "5cd3c4b8-aa5e-431a-9c55-abf83c329e70",
          "name" : "picture",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "picture",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "picture",
            "jsonType.label" : "String"
          }
        } ]
      }, {
        "id" : "dcff432a-32fe-48f3-9dec-c7f3fb8bcba2",
        "name" : "microprofile-jwt",
        "description" : "Microprofile - JWT built-in scope",
        "protocol" : "openid-connect",
        "attributes" : {
          "include.in.token.scope" : "true",
          "display.on.consent.screen" : "false"
        },
        "protocolMappers" : [ {
          "id" : "6f1d16ef-8fc0-4ba9-95bb-3df97a5563be",
          "name" : "upn",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "username",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "upn",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "fa213b18-3ba0-4578-b4ca-c23b2018bce3",
          "name" : "groups",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-realm-role-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "multivalued" : "true",
            "user.attribute" : "foo",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "groups",
            "jsonType.label" : "String"
          }
        } ]
      }, {
        "id" : "ec369c5e-2668-44e3-a9dd-5e1e223e839d",
        "name" : "address",
        "description" : "OpenID Connect built-in scope: address",
        "protocol" : "openid-connect",
        "attributes" : {
          "include.in.token.scope" : "true",
          "display.on.consent.screen" : "true",
          "consent.screen.text" : "${addressScopeConsentText}"
        },
        "protocolMappers" : [ {
          "id" : "6e0fb738-56b2-4960-bc73-6caaafbcb549",
          "name" : "address",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-address-mapper",
          "consentRequired" : false,
          "config" : {
            "user.attribute.formatted" : "formatted",
            "user.attribute.country" : "country",
            "introspection.token.claim" : "true",
            "user.attribute.postal_code" : "postal_code",
            "userinfo.token.claim" : "true",
            "user.attribute.street" : "street",
            "id.token.claim" : "true",
            "user.attribute.region" : "region",
            "access.token.claim" : "true",
            "user.attribute.locality" : "locality"
          }
        } ]
      }, {
        "id" : "cf5b9c2b-bcbe-45c2-87a9-f740912542f0",
        "name" : "phone",
        "description" : "OpenID Connect built-in scope: phone",
        "protocol" : "openid-connect",
        "attributes" : {
          "include.in.token.scope" : "true",
          "display.on.consent.screen" : "true",
          "consent.screen.text" : "${phoneScopeConsentText}"
        },
        "protocolMappers" : [ {
          "id" : "9c931216-b332-4dc5-93e0-9a981bb98791",
          "name" : "phone number",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "phoneNumber",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "phone_number",
            "jsonType.label" : "String"
          }
        }, {
          "id" : "f9c5103a-f8cd-4bb4-aa9d-9783cc9e372a",
          "name" : "phone number verified",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "phoneNumberVerified",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "phone_number_verified",
            "jsonType.label" : "boolean"
          }
        } ]
      }, {
        "id" : "4783bcb2-5b83-40c7-abb1-fa9036358bed",
        "name" : "web-origins",
        "description" : "OpenID Connect scope for add allowed web origins to the access token",
        "protocol" : "openid-connect",
        "attributes" : {
          "include.in.token.scope" : "false",
          "display.on.consent.screen" : "false",
          "consent.screen.text" : ""
        },
        "protocolMappers" : [ {
          "id" : "e56bc4e4-15ea-46ef-81c9-ef2034942f4b",
          "name" : "allowed web origins",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-allowed-origins-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "access.token.claim" : "true"
          }
        } ]
      }, {
        "id" : "ce3b827d-f01b-4e31-b81d-06b9d8fa2c38",
        "name" : "acr",
        "description" : "OpenID Connect scope for add acr (authentication context class reference) to the token",
        "protocol" : "openid-connect",
        "attributes" : {
          "include.in.token.scope" : "false",
          "display.on.consent.screen" : "false"
        },
        "protocolMappers" : [ {
          "id" : "9f5652bf-c612-4774-8080-6dadbb26e015",
          "name" : "acr loa level",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-acr-mapper",
          "consentRequired" : false,
          "config" : {
            "id.token.claim" : "true",
            "introspection.token.claim" : "true",
            "access.token.claim" : "true"
          }
        } ]
      }, {
        "id" : "c13470af-e411-426d-8cb7-ceb7e7e8777f",
        "name" : "openid",
        "description" : "",
        "protocol" : "openid-connect",
        "attributes" : {
          "include.in.token.scope" : "true",
          "display.on.consent.screen" : "true",
          "gui.order" : "",
          "consent.screen.text" : ""
        }
      }, {
        "id" : "d3b7a41d-103f-4090-a2ae-d977377d9844",
        "name" : "offline_access",
        "description" : "OpenID Connect built-in scope: offline_access",
        "protocol" : "openid-connect",
        "attributes" : {
          "consent.screen.text" : "${offlineAccessScopeConsentText}",
          "display.on.consent.screen" : "true"
        }
      }, {
        "id" : "dbbc4c5a-3457-499d-b95c-bc56156c15fb",
        "name" : "role_list",
        "description" : "SAML role list",
        "protocol" : "saml",
        "attributes" : {
          "consent.screen.text" : "${samlRoleListScopeConsentText}",
          "display.on.consent.screen" : "true"
        },
        "protocolMappers" : [ {
          "id" : "690d029c-66eb-42a8-9148-787b373c14ef",
          "name" : "role list",
          "protocol" : "saml",
          "protocolMapper" : "saml-role-list-mapper",
          "consentRequired" : false,
          "config" : {
            "single" : "false",
            "attribute.nameformat" : "Basic",
            "attribute.name" : "Role"
          }
        } ]
      }, {
        "id" : "e6c1c980-33ed-4a96-9ffc-a218857fee15",
        "name" : "email",
        "description" : "OpenID Connect built-in scope: email",
        "protocol" : "openid-connect",
        "attributes" : {
          "include.in.token.scope" : "true",
          "display.on.consent.screen" : "true",
          "consent.screen.text" : "${emailScopeConsentText}"
        },
        "protocolMappers" : [ {
          "id" : "699cec0f-5f4c-4ec4-9a73-66c3bef966ad",
          "name" : "email verified",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-property-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "emailVerified",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "email_verified",
            "jsonType.label" : "boolean"
          }
        }, {
          "id" : "dd8a9c22-2b6e-45f3-8795-91c43a5f8998",
          "name" : "email",
          "protocol" : "openid-connect",
          "protocolMapper" : "oidc-usermodel-attribute-mapper",
          "consentRequired" : false,
          "config" : {
            "introspection.token.claim" : "true",
            "userinfo.token.claim" : "true",
            "user.attribute" : "email",
            "id.token.claim" : "true",
            "access.token.claim" : "true",
            "claim.name" : "email",
            "jsonType.label" : "String"
          }
        } ]
      } ],
      "defaultDefaultClientScopes" : [ "role_list", "profile", "email", "roles", "web-origins", "acr", "openid" ],
      "defaultOptionalClientScopes" : [ "offline_access", "address", "phone", "microprofile-jwt" ],
      "browserSecurityHeaders" : {
        "contentSecurityPolicyReportOnly" : "",
        "xContentTypeOptions" : "nosniff",
        "referrerPolicy" : "no-referrer",
        "xRobotsTag" : "none",
        "xFrameOptions" : "SAMEORIGIN",
        "contentSecurityPolicy" : "frame-src 'self'; frame-ancestors 'self'; object-src 'none';",
        "xXSSProtection" : "1; mode=block",
        "strictTransportSecurity" : "max-age=31536000; includeSubDomains"
      },
      "smtpServer" : { },
      "eventsEnabled" : false,
      "eventsListeners" : [ "jboss-logging" ],
      "enabledEventTypes" : [ ],
      "adminEventsEnabled" : false,
      "adminEventsDetailsEnabled" : false,
      "identityProviders" : [ ],
      "identityProviderMappers" : [ ],
      "components" : {
        "org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy" : [ {
          "id" : "0f098cb1-4aca-4de6-880d-75d2845a6cf2",
          "name" : "Allowed Protocol Mapper Types",
          "providerId" : "allowed-protocol-mappers",
          "subType" : "authenticated",
          "subComponents" : { },
          "config" : {
            "allowed-protocol-mapper-types" : [ "saml-user-attribute-mapper", "oidc-usermodel-property-mapper", "saml-role-list-mapper", "saml-user-property-mapper", "oidc-usermodel-attribute-mapper", "oidc-sha256-pairwise-sub-mapper", "oidc-full-name-mapper", "oidc-address-mapper" ]
          }
        }, {
          "id" : "e4ebdd09-423b-4da5-9b0b-6ac1533303ec",
          "name" : "Consent Required",
          "providerId" : "consent-required",
          "subType" : "anonymous",
          "subComponents" : { },
          "config" : { }
        }, {
          "id" : "846a0c36-0864-440a-97c5-2d53c35793ec",
          "name" : "Max Clients Limit",
          "providerId" : "max-clients",
          "subType" : "anonymous",
          "subComponents" : { },
          "config" : {
            "max-clients" : [ "200" ]
          }
        }, {
          "id" : "d4d24c45-b934-4061-8a9f-13f6b65edd0d",
          "name" : "Allowed Protocol Mapper Types",
          "providerId" : "allowed-protocol-mappers",
          "subType" : "anonymous",
          "subComponents" : { },
          "config" : {
            "allowed-protocol-mapper-types" : [ "saml-user-property-mapper", "oidc-address-mapper", "saml-user-attribute-mapper", "oidc-full-name-mapper", "saml-role-list-mapper", "oidc-usermodel-attribute-mapper", "oidc-sha256-pairwise-sub-mapper", "oidc-usermodel-property-mapper" ]
          }
        }, {
          "id" : "1cecfbc5-cf78-4c00-89d7-4efb7867c0c7",
          "name" : "Allowed Client Scopes",
          "providerId" : "allowed-client-templates",
          "subType" : "authenticated",
          "subComponents" : { },
          "config" : {
            "allow-default-scopes" : [ "true" ]
          }
        }, {
          "id" : "11c2b26f-397f-47c1-b321-cc7c5b18d977",
          "name" : "Full Scope Disabled",
          "providerId" : "scope",
          "subType" : "anonymous",
          "subComponents" : { },
          "config" : { }
        }, {
          "id" : "d3543873-1e94-40cc-b103-f50421988ba8",
          "name" : "Allowed Client Scopes",
          "providerId" : "allowed-client-templates",
          "subType" : "anonymous",
          "subComponents" : { },
          "config" : {
            "allow-default-scopes" : [ "true" ]
          }
        }, {
          "id" : "07f7e09f-5926-48d0-9807-255e60af686b",
          "name" : "Trusted Hosts",
          "providerId" : "trusted-hosts",
          "subType" : "anonymous",
          "subComponents" : { },
          "config" : {
            "host-sending-registration-request-must-match" : [ "true" ],
            "client-uris-must-match" : [ "true" ]
          }
        } ],
        "org.keycloak.keys.KeyProvider" : [ {
          "id" : "c2ca3614-ccda-43b5-93ba-6f07e93c5217",
          "name" : "rsa-generated",
          "providerId" : "rsa-generated",
          "subComponents" : { },
          "config" : {
            "privateKey" : [ "MIIEowIBAAKCAQEAn8hkLOfFwxYvpvtUBMmIwk8uzI37G7oi569uJYu4BGcMh7pK80r5U3SrH6XNTG3tF6AtpUBE1+mapIC6G7KYBrXk6YyFzL7zKnvLgKol9lU5OL3DT93LyYNUc+oya2hO+bfoyUZSCEnFzVsNvI9EDNpGGkc4Lmhw/NbN7LOmC57ypmAqeRdPqk7jjUvjOqXtMmW3O8Zk6rGTftR8wTP50LW+QJsUbFs+dn7BhWl4mVliQP2DWpUAR4F1grUgDJ3UpGjgq0giP1ycKSSyVWPxipC9DwkBFJtoUnDrJCUDtTAKNWRGiPdbpUki/yb53OfHw2e8SThJ8iTbOOQDFVehBQIDAQABAoIBACP3B7Fh5A5C7qvv01wWfu15xEdvx+9YSOYMg0b5OYzoIxZj9N3paNCDqMcBW4ISFjVkrRGOs1BEOTZA8WMtonGUPLfjFPTeoD5WShqmcdAOI8ilUhKmkmnD4V0MAJhTjL9ByaiQZwONk3PjRGbGeqNH+FKO6FLt2EzZDtXyI/RmUdoyaec9p7nRer1u5y/uqwftaeFkVpcOROsGuGjRzN9MpA0hiSvNVb1NC7wenTpMd4lPYVMQFis4YwZNvBnqVN8gUVhaEu0vW9g7EotvdLvOvfyp0BQoH0G3CV2NaWpzOUsCndAtPlh/X4cX9kwISyM8rPTnxzCpGKANgor0yRsCgYEA2hlPkR1t64aspB1/uUfcFZ5X3djhMPzJPNYdA18Qu8lWM1mG6Ht7P/cDt5wrEli//P/dZ3CxWQnsHi4cKn/hDFl58vPWHLxO5Uzn+lQF2kDiPGMw7Iudr81gomb23WcUGdjNebH1I+V3woP0oHmGNvU4xALuAQsxukdvzsTKU28CgYEAu4y9Oldfyu/jhkTuJCerSm0y9uISULjDS+csBGXxedRcQ+brzPSoeAA1eVJuEu++JVWNf6qXa8wWDYClhD3mC63/DRTP7cRVwnXDI63b+aRviV4KRd+g18Bdy42bkXX4H9avlcCueY+Pkca9UsAV480aIf4Z+Bfxugn/wZh0CMsCgYBhtNUgfb6wtTTCkTSXHMpMJRPYH0FDYPwGdSWqTPyCJECp4IG/xRICJfdUWnIxVfEstrPJk4LgEMHnA2E66O85SRyI6xQQ64uszjdeviWhEVBJHWbdke6iw693EgmmRneK3MWwrzjTTAnK6rK88bRfCpzTszxM22b7vOUqDJ9XoQKBgDCLLhNiuC2rx8ViJA2BowlhmFZkj38LQ6lqzW7aABWbjMIajCABl3VwAGdFma4XFa1OJpDL0+OX8IFQMgc3ikqtn52ALI2LSvNJTdf3UT+dey0gBSKuzk/pv51HKuQMl8uN4uas97wJF40Q1sILa98JoGD6Bzi64dtYaTVvw2DfAoGBALP1bkE9Gq/Rux8YILMncohXR+g1KVsW20x+6Q+xVgf9u0YJc0deSfsnSPQYdvhCZ3jBlvkEWsaAxLlCWpq8sWnl+jajPyla+RVs7Z/MFKfYixoRcUQIWs5M07P22rzQ5pGEBW4UGScjK14slGl0VNJ20IMT79xdC0vFZemB5lan" ],
            "keyUse" : [ "SIG" ],
            "certificate" : [ "MIICtTCCAZ0CBgGVRwgsszANBgkqhkiG9w0BAQsFADAeMRwwGgYDVQQDDBNtaWNyb3NlcnZpY2VzLXJlYWxtMB4XDTI1MDIyNzEwNDkzMloXDTM1MDIyNzEwNTExMlowHjEcMBoGA1UEAwwTbWljcm9zZXJ2aWNlcy1yZWFsbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJ/IZCznxcMWL6b7VATJiMJPLsyN+xu6IuevbiWLuARnDIe6SvNK+VN0qx+lzUxt7RegLaVARNfpmqSAuhuymAa15OmMhcy+8yp7y4CqJfZVOTi9w0/dy8mDVHPqMmtoTvm36MlGUghJxc1bDbyPRAzaRhpHOC5ocPzWzeyzpgue8qZgKnkXT6pO441L4zql7TJltzvGZOqxk37UfMEz+dC1vkCbFGxbPnZ+wYVpeJlZYkD9g1qVAEeBdYK1IAyd1KRo4KtIIj9cnCkkslVj8YqQvQ8JARSbaFJw6yQlA7UwCjVkRoj3W6VJIv8m+dznx8NnvEk4SfIk2zjkAxVXoQUCAwEAATANBgkqhkiG9w0BAQsFAAOCAQEAAHYJHxq2VA+ByLGe5cFpsZhVYsc7R/uZPqn5DCypCjjSMWa3UrbwsYC1QktAMSJiljyJOFP2olwGiomdUGID31pO/uxbghj9SIShzwQidAlf9LE12ZTBJNyXXEL/aQEfjEZWvh1SRSOdlA+RAmu2wr6PjPxuzeKyQCRyMmEzJOp+z2E55KOJMWyqqYO6+snxebobDIkdQLAsbax1fcQTtLH/nKB+V0dcp870zWp6MZa/ROM6MmDOXWbGZtV7NWYQ8DjHUsklaFVXpN3qPIr7UCEI0Xl8qi2OcxE682mOj+9U8GAAk9uPedzm7MAIoQNcwDQ/VU46QpkamdNbEw4+bw==" ],
            "priority" : [ "100" ]
          }
        }, {
          "id" : "169a4a49-bbb5-4959-aacd-01012dea6c02",
          "name" : "rsa-enc-generated",
          "providerId" : "rsa-enc-generated",
          "subComponents" : { },
          "config" : {
            "privateKey" : [ "MIIEowIBAAKCAQEArsBlJ2qWrCDRHhnvKImNzWOruTdOG3/ovz8OjjWmt/EQbPa8SKMNPFVezBnHXsL7aBGK6Ustxe6vzoXprfKyN5b+AbV+4iix/hlksjZ6awWIMMgq+4rG3tmGFFgL1bE3b5gzNsS/MZ5ajUAbhfN5w9WqpMTiEPPa5mEJEKxAYO9McCD58P7T9456rrx4Cerv37/NJf0vTRPnpU3u///Nt6ZC87dSkUUyBcLRWlTiVW4nY/UAiWOK1wm3x9nN7hhCrKA1kMLl8D0ejs0RIWcTTYue8oExmiVt/OJ0H0KymFscUudY0c/qBEx7rscgP5gppSajny30WuIq60naodJ/nQIDAQABAoIBAAlWKzFjbwBsUm/ux8ndK4/dMugW7dvnRhqttkvfzvQF87A2sJGa8JKYJxP1jqLf/Qmu4kKbqyrnmUoJ+pW6IU56CUEzHr/bl8LxPWGOKpU8Ou4MS8ubuwteCN1eZOd6a/FQ6u+SrHpssP7Z/X53JzrQ2Cisgj2QodHsPk0/PsNO/xp7t6oCFdlIWfcbUVL5QNACy7dSuzqx5UgIg5TD8eZ48bJGMfKNTISyrfkZi+8JiiJdbXJhk9J0urinY+HJdXr2bBJeYEque9SYvu38THDjOI1I28oKBCAbrEUeQsIO36IoNhDf/u7/ke9NxUlE9WNXFOvKQ5Mq6TmgeyIDFGECgYEA4esfNODC+UzJn2odaP22VtyoqekW8O/UgNhJ2M5v0KDQocMX3E9kMo9WeBW0+lVDb8EhLDAN0j/xq/9bELLe1kL8v3y9L63pADw2GmN6uj8LAuxQ6hC/fDkj3EL6gFEvefiFr1agJjwqRvTGOuP1qQsUrXeY8vaPp1thfdO4WLECgYEAxgUl4EftMlZiNgCkuuAA/yEq+Ig1RfkmG1GCTi013dcOWhKxEMwKqpSROJEZDLJOFzyA3JaHBRlWndT9LtVtmwbole7+wS9fYHI0cbLUhNY7McrwMRlVHeDbjeVh1L68FqvWzpQ5BwIcXKeZ1rI3rMaiQ7w9paRVHgHo9wjLkK0CgYEAto5mczZEl2tjabw388VA+MPqTxKZhSxnzY35boayeCXbMTwTJeXJk8mcGAGCct8/VSj9A1X5dTJSuafpEH9Z7k3HK31C6ypfI9+D4KMOYz3aSgxn/hx2f4002yIMhnAUKz2V2W/ojb0EdL4LCT94HZdkqCACqiJvRs9i4miZiKECgYAICYV0CPdTp7XswzHsc9MWh94aoEypPmeRjElhquwYyPXIKiINsHpuWgFa7mi53zLUNpx0V5e7WT/uwApWsEr2k9002LJ2XNneLER8qOl/SsK0TqxZEdImmE3RAJJr9CHmOatKTzHRWwCTsinIUQt5c8Z6QVWvN3BM+37dJ/o7MQKBgAEHtZ+RvSx9Z6HV9a3lUUX0d7kJWLI30T96i+fA57bsf5OtHzc46DRAB1EnyyD86iCWjhHLJJq9FsXImiyOG/EVcR1N3vBm9AbkmNe2T215Z9H9fy9wx9rNBFaEMVTiw09JHsBQxf9DMGUQ7bT3BeI6216cGgnSGRQ9UlTKvvsF" ],
            "keyUse" : [ "ENC" ],
            "certificate" : [ "MIICtTCCAZ0CBgGVRwgs+DANBgkqhkiG9w0BAQsFADAeMRwwGgYDVQQDDBNtaWNyb3NlcnZpY2VzLXJlYWxtMB4XDTI1MDIyNzEwNDkzM1oXDTM1MDIyNzEwNTExM1owHjEcMBoGA1UEAwwTbWljcm9zZXJ2aWNlcy1yZWFsbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAK7AZSdqlqwg0R4Z7yiJjc1jq7k3Tht/6L8/Do41prfxEGz2vEijDTxVXswZx17C+2gRiulLLcXur86F6a3ysjeW/gG1fuIosf4ZZLI2emsFiDDIKvuKxt7ZhhRYC9WxN2+YMzbEvzGeWo1AG4XzecPVqqTE4hDz2uZhCRCsQGDvTHAg+fD+0/eOeq68eAnq79+/zSX9L00T56VN7v//zbemQvO3UpFFMgXC0VpU4lVuJ2P1AIljitcJt8fZze4YQqygNZDC5fA9Ho7NESFnE02LnvKBMZolbfzidB9CsphbHFLnWNHP6gRMe67HID+YKaUmo58t9FriKutJ2qHSf50CAwEAATANBgkqhkiG9w0BAQsFAAOCAQEArEp9h9TesFHTI5cQjpUHxn4C439H/lCqa2OcCKXNOZL/y2TtFGJF3X/X/obZgjNemILtEuazURcpxqnR5iomB3vFA8FzD9qlHGAXG1Lw6F99kOs3sQSPZ8wshDHmYxFMvZY7KVfqmjkHnwmh6cOxVDKbRjSMwG0ASpfLXdm29EDUPiu1e/7MXi2kdf1YnVIlG01R/I67YXeqniXtWQXq61RY49hfv4+LG2G9stPVOPVgCjZbt0NAyKWx5QKijf39PskEctBktV3qNXquK1bPRAd/cHicjaJUfpFfJH+sB7QZ6Au8J1o3mNOzMevF+qNefWkiouFWNwdm4yqKDpWGUw==" ],
            "priority" : [ "100" ],
            "algorithm" : [ "RSA-OAEP" ]
          }
        }, {
          "id" : "500a72f0-a72e-4e40-9ef2-688078a0fd05",
          "name" : "aes-generated",
          "providerId" : "aes-generated",
          "subComponents" : { },
          "config" : {
            "kid" : [ "bfa0f690-d420-4ea2-ab41-e60dc1391bc5" ],
            "secret" : [ "vwg6YRUaSW3kvnJPGVDp4Q" ],
            "priority" : [ "100" ]
          }
        }, {
          "id" : "e90bb1ea-70b6-4ffc-a96e-d451a18a6566",
          "name" : "hmac-generated",
          "providerId" : "hmac-generated",
          "subComponents" : { },
          "config" : {
            "kid" : [ "90e0befa-dbc3-4a48-960c-7c633ecb1120" ],
            "secret" : [ "PJSSThdwQnnmi8tkI5gHUUyFZq9M7eVOxD8jbF2Fdp8eufNezGteQNE-rripUve2p-So0S8aDP3FriOVGA_JyA" ],
            "priority" : [ "100" ],
            "algorithm" : [ "HS256" ]
          }
        } ]
      },
      "internationalizationEnabled" : false,
      "supportedLocales" : [ ],
      "authenticationFlows" : [ {
        "id" : "6105faea-6a43-4741-bce8-02ea429bd8ec",
        "alias" : "Account verification options",
        "description" : "Method with which to verity the existing account",
        "providerId" : "basic-flow",
        "topLevel" : false,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "idp-email-verification",
          "authenticatorFlow" : false,
          "requirement" : "ALTERNATIVE",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticatorFlow" : true,
          "requirement" : "ALTERNATIVE",
          "priority" : 20,
          "autheticatorFlow" : true,
          "flowAlias" : "Verify Existing Account by Re-authentication",
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "fdd009ab-0a1d-46f4-a976-b7a00c4075f6",
        "alias" : "Browser - Conditional OTP",
        "description" : "Flow to determine if the OTP is required for the authentication",
        "providerId" : "basic-flow",
        "topLevel" : false,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "conditional-user-configured",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "auth-otp-form",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 20,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "8afc698b-25b4-402c-89f8-1d78bd6eab2a",
        "alias" : "Direct Grant - Conditional OTP",
        "description" : "Flow to determine if the OTP is required for the authentication",
        "providerId" : "basic-flow",
        "topLevel" : false,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "conditional-user-configured",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "direct-grant-validate-otp",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 20,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "7c3cea93-a5b6-408f-813c-b71877cec8ce",
        "alias" : "First broker login - Conditional OTP",
        "description" : "Flow to determine if the OTP is required for the authentication",
        "providerId" : "basic-flow",
        "topLevel" : false,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "conditional-user-configured",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "auth-otp-form",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 20,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "16a40b49-ef5d-4116-96bf-504d528a96c6",
        "alias" : "Handle Existing Account",
        "description" : "Handle what to do if there is existing account with same email/username like authenticated identity provider",
        "providerId" : "basic-flow",
        "topLevel" : false,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "idp-confirm-link",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticatorFlow" : true,
          "requirement" : "REQUIRED",
          "priority" : 20,
          "autheticatorFlow" : true,
          "flowAlias" : "Account verification options",
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "7a93bfd3-2986-42fb-b725-cdd3630e88ce",
        "alias" : "Reset - Conditional OTP",
        "description" : "Flow to determine if the OTP should be reset or not. Set to REQUIRED to force.",
        "providerId" : "basic-flow",
        "topLevel" : false,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "conditional-user-configured",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "reset-otp",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 20,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "f6289501-7b41-42d1-9d58-2338896bcf85",
        "alias" : "User creation or linking",
        "description" : "Flow for the existing/non-existing user alternatives",
        "providerId" : "basic-flow",
        "topLevel" : false,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticatorConfig" : "create unique user config",
          "authenticator" : "idp-create-user-if-unique",
          "authenticatorFlow" : false,
          "requirement" : "ALTERNATIVE",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticatorFlow" : true,
          "requirement" : "ALTERNATIVE",
          "priority" : 20,
          "autheticatorFlow" : true,
          "flowAlias" : "Handle Existing Account",
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "ba8893aa-10b7-4f72-90e6-8206d9edb80f",
        "alias" : "Verify Existing Account by Re-authentication",
        "description" : "Reauthentication of existing account",
        "providerId" : "basic-flow",
        "topLevel" : false,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "idp-username-password-form",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticatorFlow" : true,
          "requirement" : "CONDITIONAL",
          "priority" : 20,
          "autheticatorFlow" : true,
          "flowAlias" : "First broker login - Conditional OTP",
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "4cf78886-8b17-4f8e-a699-26b34183e691",
        "alias" : "browser",
        "description" : "browser based authentication",
        "providerId" : "basic-flow",
        "topLevel" : true,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "auth-cookie",
          "authenticatorFlow" : false,
          "requirement" : "ALTERNATIVE",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "auth-spnego",
          "authenticatorFlow" : false,
          "requirement" : "DISABLED",
          "priority" : 20,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "identity-provider-redirector",
          "authenticatorFlow" : false,
          "requirement" : "ALTERNATIVE",
          "priority" : 25,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticatorFlow" : true,
          "requirement" : "ALTERNATIVE",
          "priority" : 30,
          "autheticatorFlow" : true,
          "flowAlias" : "forms",
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "c7199384-f8fc-444e-9bf0-a55492125e47",
        "alias" : "clients",
        "description" : "Base authentication for clients",
        "providerId" : "client-flow",
        "topLevel" : true,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "client-secret",
          "authenticatorFlow" : false,
          "requirement" : "ALTERNATIVE",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "client-jwt",
          "authenticatorFlow" : false,
          "requirement" : "ALTERNATIVE",
          "priority" : 20,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "client-secret-jwt",
          "authenticatorFlow" : false,
          "requirement" : "ALTERNATIVE",
          "priority" : 30,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "client-x509",
          "authenticatorFlow" : false,
          "requirement" : "ALTERNATIVE",
          "priority" : 40,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "eb65f3dd-4bb7-4d68-ae9e-df6ce0cfde37",
        "alias" : "direct grant",
        "description" : "OpenID Connect Resource Owner Grant",
        "providerId" : "basic-flow",
        "topLevel" : true,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "direct-grant-validate-username",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "direct-grant-validate-password",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 20,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticatorFlow" : true,
          "requirement" : "CONDITIONAL",
          "priority" : 30,
          "autheticatorFlow" : true,
          "flowAlias" : "Direct Grant - Conditional OTP",
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "adf40eeb-6a18-4f0c-beda-5f6c4e802842",
        "alias" : "docker auth",
        "description" : "Used by Docker clients to authenticate against the IDP",
        "providerId" : "basic-flow",
        "topLevel" : true,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "docker-http-basic-authenticator",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "2d0df7a9-2408-47d7-ac04-a3c800b6550a",
        "alias" : "first broker login",
        "description" : "Actions taken after first broker login with identity provider account, which is not yet linked to any Keycloak account",
        "providerId" : "basic-flow",
        "topLevel" : true,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticatorConfig" : "review profile config",
          "authenticator" : "idp-review-profile",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticatorFlow" : true,
          "requirement" : "REQUIRED",
          "priority" : 20,
          "autheticatorFlow" : true,
          "flowAlias" : "User creation or linking",
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "f262bd12-72a6-4eb2-85a1-0d3deb1bfed1",
        "alias" : "forms",
        "description" : "Username, password, otp and other auth forms.",
        "providerId" : "basic-flow",
        "topLevel" : false,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "auth-username-password-form",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticatorFlow" : true,
          "requirement" : "CONDITIONAL",
          "priority" : 20,
          "autheticatorFlow" : true,
          "flowAlias" : "Browser - Conditional OTP",
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "3d9e5c42-0b68-45ee-93c4-185fd06859c8",
        "alias" : "registration",
        "description" : "registration flow",
        "providerId" : "basic-flow",
        "topLevel" : true,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "registration-page-form",
          "authenticatorFlow" : true,
          "requirement" : "REQUIRED",
          "priority" : 10,
          "autheticatorFlow" : true,
          "flowAlias" : "registration form",
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "b7a8aaae-e568-4fbb-a7d6-c755c269647f",
        "alias" : "registration form",
        "description" : "registration form",
        "providerId" : "form-flow",
        "topLevel" : false,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "registration-user-creation",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 20,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "registration-password-action",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 50,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "registration-recaptcha-action",
          "authenticatorFlow" : false,
          "requirement" : "DISABLED",
          "priority" : 60,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "04d082bb-e7b7-41da-8ef0-22cadc33a074",
        "alias" : "reset credentials",
        "description" : "Reset credentials for a user if they forgot their password or something",
        "providerId" : "basic-flow",
        "topLevel" : true,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "reset-credentials-choose-user",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "reset-credential-email",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 20,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticator" : "reset-password",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 30,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        }, {
          "authenticatorFlow" : true,
          "requirement" : "CONDITIONAL",
          "priority" : 40,
          "autheticatorFlow" : true,
          "flowAlias" : "Reset - Conditional OTP",
          "userSetupAllowed" : false
        } ]
      }, {
        "id" : "6834cc86-7bbd-4a05-b7d7-9802d307637d",
        "alias" : "saml ecp",
        "description" : "SAML ECP Profile Authentication Flow",
        "providerId" : "basic-flow",
        "topLevel" : true,
        "builtIn" : true,
        "authenticationExecutions" : [ {
          "authenticator" : "http-basic-authenticator",
          "authenticatorFlow" : false,
          "requirement" : "REQUIRED",
          "priority" : 10,
          "autheticatorFlow" : false,
          "userSetupAllowed" : false
        } ]
      } ],
      "authenticatorConfig" : [ {
        "id" : "de226ecb-ca01-4aac-acb7-9fe4f607ffe8",
        "alias" : "create unique user config",
        "config" : {
          "require.password.update.after.registration" : "false"
        }
      }, {
        "id" : "763f77ba-bf19-4c0f-aa07-4ddfa18b850b",
        "alias" : "review profile config",
        "config" : {
          "update.profile.on.first.login" : "missing"
        }
      } ],
      "requiredActions" : [ {
        "alias" : "CONFIGURE_TOTP",
        "name" : "Configure OTP",
        "providerId" : "CONFIGURE_TOTP",
        "enabled" : false,
        "defaultAction" : false,
        "priority" : 10,
        "config" : { }
      }, {
        "alias" : "TERMS_AND_CONDITIONS",
        "name" : "Terms and Conditions",
        "providerId" : "TERMS_AND_CONDITIONS",
        "enabled" : false,
        "defaultAction" : false,
        "priority" : 20,
        "config" : { }
      }, {
        "alias" : "UPDATE_PASSWORD",
        "name" : "Update Password",
        "providerId" : "UPDATE_PASSWORD",
        "enabled" : false,
        "defaultAction" : false,
        "priority" : 30,
        "config" : { }
      }, {
        "alias" : "UPDATE_PROFILE",
        "name" : "Update Profile",
        "providerId" : "UPDATE_PROFILE",
        "enabled" : false,
        "defaultAction" : false,
        "priority" : 40,
        "config" : { }
      }, {
        "alias" : "VERIFY_EMAIL",
        "name" : "Verify Email",
        "providerId" : "VERIFY_EMAIL",
        "enabled" : false,
        "defaultAction" : false,
        "priority" : 50,
        "config" : { }
      }, {
        "alias" : "delete_account",
        "name" : "Delete Account",
        "providerId" : "delete_account",
        "enabled" : false,
        "defaultAction" : false,
        "priority" : 60,
        "config" : { }
      }, {
        "alias" : "webauthn-register",
        "name" : "Webauthn Register",
        "providerId" : "webauthn-register",
        "enabled" : false,
        "defaultAction" : false,
        "priority" : 70,
        "config" : { }
      }, {
        "alias" : "webauthn-register-passwordless",
        "name" : "Webauthn Register Passwordless",
        "providerId" : "webauthn-register-passwordless",
        "enabled" : false,
        "defaultAction" : false,
        "priority" : 80,
        "config" : { }
      }, {
        "alias" : "update_user_locale",
        "name" : "Update User Locale",
        "providerId" : "update_user_locale",
        "enabled" : false,
        "defaultAction" : false,
        "priority" : 1000,
        "config" : { }
      } ],
      "browserFlow" : "browser",
      "registrationFlow" : "registration",
      "directGrantFlow" : "direct grant",
      "resetCredentialsFlow" : "reset credentials",
      "clientAuthenticationFlow" : "clients",
      "dockerAuthenticationFlow" : "docker auth",
      "attributes" : {
        "cibaBackchannelTokenDeliveryMode" : "poll",
        "cibaAuthRequestedUserHint" : "login_hint",
        "oauth2DevicePollingInterval" : "10",
        "clientOfflineSessionMaxLifespan" : "0",
        "clientSessionIdleTimeout" : "0",
        "actionTokenGeneratedByUserLifespan.verify-email" : "",
        "actionTokenGeneratedByUserLifespan.idp-verify-account-via-email" : "",
        "clientOfflineSessionIdleTimeout" : "0",
        "actionTokenGeneratedByUserLifespan.execute-actions" : "",
        "cibaInterval" : "5",
        "realmReusableOtpCode" : "false",
        "cibaExpiresIn" : "120",
        "oauth2DeviceCodeLifespan" : "1500",
        "parRequestUriLifespan" : "60",
        "clientSessionMaxLifespan" : "0",
        "shortVerificationUri" : "",
        "actionTokenGeneratedByUserLifespan.reset-credentials" : ""
      },
      "keycloakVersion" : "23.0.6",
      "userManagedAccessAllowed" : false,
      "clientProfiles" : {
        "profiles" : [ ]
      },
      "clientPolicies" : {
        "policies" : [ ]
      }
    }
{{- end }}
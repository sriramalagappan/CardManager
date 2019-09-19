package com.example.ewallet.Util;

public class UserErrorHandler {

    private String errorResponse;

    public UserErrorHandler(String error) {
        switch (error) {
            case "ERROR_INVALID_CUSTOM_TOKEN":
                errorResponse = ("The custom token format is incorrect. Please check the documentation.");
                break;

            case "ERROR_CUSTOM_TOKEN_MISMATCH":
                errorResponse = ("The custom token corresponds to a different audience.");
                break;

            case "ERROR_INVALID_CREDENTIAL":
                errorResponse = ("The supplied auth credential is malformed or has expired");
                break;

            case "ERROR_INVALID_EMAIL":
                errorResponse = ("Invalid Email Address. Please try again");
                break;

            case "ERROR_WRONG_PASSWORD":
                errorResponse = ("Password is incorrect. Please try again or click 'Forgot Password'");
                break;

            case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                errorResponse = ("An account already exists with the same email address but different sign-in credentials");
                break;

            case "ERROR_EMAIL_ALREADY_IN_USE":
                errorResponse = ("This email account is already associated with a user");
                break;

            case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                errorResponse = ("This credential is already associated with a different user account.");
                break;

            case "ERROR_USER_DISABLED":
                errorResponse = ("The user account has been disabled by an administrator.");
                break;

            case "ERROR_USER_TOKEN_EXPIRED":
            case "ERROR_INVALID_USER_TOKEN":
                errorResponse = ("The user\\'s credential is no longer valid. The user must sign in again.");
                break;

            case "ERROR_USER_NOT_FOUND":
                errorResponse = ("There is no user associated to that email. Please check or try a different email.");
                break;

            case "ERROR_OPERATION_NOT_ALLOWED":
                errorResponse = ("This operation is not allowed. You must enable this service in the console.");
                break;

            default:
                errorResponse = ("Please try again");
                break;
        }
    }

    public String getErrorResponse() {
        return errorResponse;
    }
}

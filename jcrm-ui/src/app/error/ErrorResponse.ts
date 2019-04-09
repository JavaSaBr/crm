import {HttpErrorResponse} from "@angular/common/http";

export class ErrorResponse {

    public static convertToErrorCodeOrNull(resp: HttpErrorResponse): number | null {
        if (resp.ok) {
            return null;
        } else if (resp.status == 400) {
            const json = resp.error as ErrorResponse;
            return json.errorCode;
        } else {
            throw new Error(resp.message);
        }
    }

    public static convertToErrorOrNull(resp: HttpErrorResponse): ErrorResponse | null {
        if (resp.ok) {
            return null;
        } else if (resp.status == 400) {
            return resp.error as ErrorResponse;
        } else {
            throw new Error(resp.message);
        }
    }

    errorCode: number;
    errorMessage: string;
}
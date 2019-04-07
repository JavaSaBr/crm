import {HttpResponse} from "@angular/common/http";

export class ErrorResponse {

    public static convertToErrorCodeOrNull(resp: HttpResponse<{}>): number | null {
        if (resp.ok) {
            return null;
        } else if (resp.status == 400) {
            const body = resp.body as ErrorResponse;
            return body.errorCode;
        } else {
            throw new Error(resp.statusText);
        }
    }

    errorCode: number;
    errorMessage: string;
}
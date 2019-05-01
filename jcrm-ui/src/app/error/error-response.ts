import {HttpErrorResponse} from "@angular/common/http";
import {TranslateService} from '@ngx-translate/core';

export class ErrorResponse {

    public static convertToErrorOrNull(resp: HttpErrorResponse, translateService: TranslateService): ErrorResponse | null {
        if (resp.ok) {
            return null;
        } else if (resp.status >= 400 && resp.status < 500) {

            let error = resp.error as ErrorResponse;
            let errorMessage = translateService.instant('SERVER.ERROR.' + error.errorCode) as string;

            if (!errorMessage.startsWith('SERVER.ERROR')) {
                return new ErrorResponse(error.errorCode, errorMessage);
            }

            return error;

        } else {
            return new ErrorResponse(resp.status, resp.statusText);
        }
    }

    errorCode: number;
    errorMessage: string;

    constructor(errorCode: number = 0, errorMessage: string = null) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

import {HttpErrorResponse} from '@angular/common/http';
import {TranslateService} from '@ngx-translate/core';
import {Utils} from '@app/util/utils';

export class ErrorResponse {

    public static convertToErrorOrNull(resp: HttpErrorResponse, translateService: TranslateService): ErrorResponse | null {

        if (resp.ok) {
            return null;
        }

        console.log(`Error response: ${Utils.toString(resp)}`);

        if (resp.status >= 400 && resp.status < 500) {

            let error = resp.error as ErrorResponse;
            let errorMessage = translateService.instant(`SERVER.ERROR.${error.errorCode}`) as string;

            if (!errorMessage.startsWith('SERVER.ERROR')) {
                return new ErrorResponse(error.errorCode, errorMessage);
            } else if (error.errorMessage) {
                return new ErrorResponse(error.errorCode, error.errorMessage);
            }

            return error;

        } else {
            return new ErrorResponse(resp.status, resp.statusText);
        }
    }

    errorCode: number;
    errorMessage: string | null;

    constructor(errorCode: number = 0, errorMessage: string | null = null) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

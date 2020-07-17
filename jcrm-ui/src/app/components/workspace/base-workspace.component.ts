import {AfterViewInit} from '@app/node-modules/@angular/core';
import {WorkspaceService} from '@app/service/workspace.service';
import {Directive} from '@angular/core';

@Directive()
export abstract class BaseWorkspaceComponent implements AfterViewInit {

    protected constructor(protected readonly workspaceService: WorkspaceService) {
    }

    ngAfterViewInit(): void {
        setTimeout(() => this.workspaceService.activate(this));
    }

    getTitle(): string | null {
        return null;
    }

    isFullScreen(): boolean {
        return false;
    }

    isNeedGlobalSearch(): boolean {
        return false;
    }

    isNeedGlobalMenu(): boolean {
        return false;
    }

    isNeedLeftRightPadding(): boolean {
        return false;
    }

    isNeedTopBottomPadding(): boolean {
        return false;
    }
}

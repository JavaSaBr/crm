import {AfterViewInit, Directive, ElementRef} from '@angular/core';

@Directive({
    selector: '[app-autofocus]'
})
export class AutofocusDirective implements AfterViewInit {

    constructor(private elementRef: ElementRef) {
    }

    ngAfterViewInit() {
        setTimeout(() => this.elementRef.nativeElement.focus(), 50);
    }
}

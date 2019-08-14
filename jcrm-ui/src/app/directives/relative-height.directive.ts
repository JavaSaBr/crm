import {AfterViewInit, Directive, ElementRef, HostListener, Input} from '@angular/core';

@Directive({
    selector: '[app-relative-height]'
})
export class RelativeHeightDirective implements AfterViewInit {

    @Input()
    relativeHeightOffset: any;

    lastWindowResizedTime: number = -1;

    constructor(private elementRef: ElementRef) {
    }

    ngAfterViewInit() {

        let htmlElement: HTMLElement = this.elementRef.nativeElement;
        htmlElement.hidden = true;

        setTimeout(() => {
            this.updateHeight(htmlElement);
        }, 100);
    }

    updateHeight(htmlElement: HTMLElement) {

        const parentElement = htmlElement && htmlElement.parentElement;
        const htmlCollection = parentElement && parentElement.children;

        if (!htmlCollection) {
            return;
        }

        htmlElement.style.maxHeight = 'initial';
        htmlElement.style.minHeight = 'initial';

        let children = Array.from(htmlCollection)
            .filter(element => element instanceof HTMLElement)
            .filter(element => element.id != htmlElement.id)
            .filter(value => value.getAttribute("relative-height") != 'ignore')
            .map(element => element as HTMLElement);

        const parentRectangle = parentElement.getBoundingClientRect();
        const height = parentRectangle.height;
        const consumedHeight = children.length < 1 ? 0 : children
            .map(element => element.getBoundingClientRect())
            .map(rectangle => rectangle.height)
            .reduce((previousValue, currentValue) => currentValue + previousValue);

        const offset = this.relativeHeightOffset ? 0 : Number.parseInt(this.relativeHeightOffset);
        const newHeight = height - consumedHeight - offset;

        htmlElement.style.maxHeight = `${newHeight}px`;
        htmlElement.style.minHeight = `${newHeight}px`;
        htmlElement.hidden = false;
    }

    @HostListener('window:resize')
    onResize() {

        let currentTime = Date.now();
        if (currentTime - this.lastWindowResizedTime < 50) {
            return;
        }

        this.lastWindowResizedTime = currentTime;

        let htmlElement: HTMLElement = this.elementRef.nativeElement;
        htmlElement.hidden = true;

        setTimeout(() => {
            this.updateHeight(htmlElement);
        }, 10);
    }
}

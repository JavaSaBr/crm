import { ElementRef, Input, OnDestroy, Directive } from '@angular/core';
import {ControlValueAccessor, NgControl} from '@angular/forms';
import {Subject} from 'rxjs';
import {FocusMonitor} from '@angular/cdk/a11y';
import {coerceBooleanProperty} from '@angular/cdk/coercion';
import {MatFormFieldControl} from '@app/node-modules/@angular/material/form-field';

@Directive()
export class BaseInput<T> implements MatFormFieldControl<T>, OnDestroy, ControlValueAccessor {

    static nextId = 0;

    protected static makeId(): number {
        return BaseInput.nextId++;
    }

    public readonly id: string;
    public readonly stateChanges: Subject<void>;

    @Input('hidePlaceholderOnFocus')
    public hidePlaceholderOnFocus: boolean = false;

    public describedBy = '';
    public focused = false;
    public autofilled = false;

    private _placeholder = '';
    private _required = false;
    private _disabled = false;

    protected onChange = (value: T) => {};
    protected onTouched = () => {};

    constructor(
        public ngControl: NgControl,
        protected focusMonitor: FocusMonitor,
        protected elementRef: ElementRef<HTMLElement>
    ) {
        this.id = this.controlType + '-' + BaseInput.makeId();
        this.stateChanges = new Subject<void>();

        if (ngControl != null) {
            ngControl.valueAccessor = this;
        }

        focusMonitor.monitor(elementRef.nativeElement, true)
            .subscribe(origin => {
                this.focused = !!origin;
                this.stateChanges.next();
                if (!this.focused) {
                    this.onTouched();
                }
            });
    }

    @Input()
    get value(): T | null {
        throw new Error('Not yet implemented');
    }

    set value(newValue: T | null) {
        throw new Error('Not yet implemented');
    }

    get controlType(): string {
        throw new Error('Not yet implemented');
    }

    get empty(): boolean {
        throw new Error('Not yet implemented');
    }

    get errorState(): boolean {
        return !this.empty && !this.ngControl.valid;
    }

    get shouldLabelFloat() {
        return this.focused || !this.empty;
    }

    @Input()
    get placeholder() {
        if (this.hidePlaceholderOnFocus && this.focused) {
            return '';
        } else {
            return this._placeholder;
        }
    }

    set placeholder(placeholder: string) {
        this._placeholder = placeholder;
        this.stateChanges.next();
    }

    @Input()
    get required() {
        return this._required;
    }

    set required(required) {
        this._required = coerceBooleanProperty(required);
        this.stateChanges.next();
    }

    @Input()
    get disabled() {
        return this._disabled;
    }

    set disabled(disabled) {
        this._disabled = coerceBooleanProperty(disabled);
        this.stateChanges.next();
    }

    registerOnChange(fn: (value: T) => void): void {
        this.onChange = fn;
    }

    registerOnTouched(fn: () => void): void {
        this.onTouched = fn;
    }

    writeValue(value: any): void {
        this.onChange(this.value);
    }

    ngOnDestroy() {
        this.stateChanges.complete();
        this.focusMonitor.stopMonitoring(this.elementRef.nativeElement);
    }

    setDescribedByIds(ids: string[]) {
        this.describedBy = ids.join(' ');
    }

    onContainerClick(event: MouseEvent) {
        if ((event.target as Element).tagName.toLowerCase() != 'input') {
            this.elementRef.nativeElement.querySelector('input').focus();
        }
    }
}

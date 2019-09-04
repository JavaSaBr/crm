import {BaseInput} from '@app/input/base-input';
import {AbstractControl, FormBuilder, FormGroup, NgControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {ElementRef, Input, OnInit, Optional, Self} from '@angular/core';
import {FocusMonitor} from '@angular/cdk/a11y';

export abstract class SingleEntityInput<T> extends BaseInput<T> implements OnInit {

    protected readonly formGroup: FormGroup;
    protected readonly entityControl: AbstractControl;

    protected _availableEntities: Observable<T[]>;
    protected _entity: T | null;

    protected constructor(
        formBuilder: FormBuilder,
        @Optional() @Self() ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>
    ) {
        super(ngControl, focusMonitor, elementRef);

        this._entity = null;
        this._availableEntities = null;
        this.formGroup = formBuilder.group({entity: ''});

        this.entityControl = this.formGroup.controls['entity'];
        this.entityControl.valueChanges
            .subscribe(value => this.extractEntity(value));

    }

    set entity(entity: T | null) {
        this._entity = entity;
        this.changeFromSubControls();
    }

    get entity(): T | null {
        return this._entity;
    }

    get empty(): boolean {
        return !this.entityControl.value;
    }

    get availableEntities(): Observable<T[]> {
        return this._availableEntities;
    }

    @Input()
    get value(): T | null {
        return this.entity;
    }

    set value(entity: T | null) {

        if (entity != null) {
            this.entityControl.setValue(entity);
        } else {
            this.entityControl.setValue(null);
        }

        this.stateChanges.next();
    }

    private changeFromSubControls(): void {
        this.stateChanges.next();
        this.onChange(this.value);
    }

    protected abstract installAutoComplete(): Observable<T[]>;

    protected abstract extractEntity(value: any): void;

    protected abstract displayWith(entity?: T): string;

    ngOnInit(): void {
        this._availableEntities = this.installAutoComplete();
    }
}

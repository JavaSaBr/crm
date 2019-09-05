import {BaseInput} from '@app/input/base-input';
import {FormControl, NgControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {ElementRef, Input, OnInit, Optional, Self} from '@angular/core';
import {FocusMonitor} from '@angular/cdk/a11y';
import {MatAutocompleteSelectedEvent} from '@angular/material';

export abstract class SingleEntityInput<T> extends BaseInput<T> implements OnInit {

    protected readonly entityControl: FormControl;

    protected _availableEntities: Observable<T[]>;
    protected _entity: T | null;

    protected constructor(
        @Optional() @Self() ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>
    ) {
        super(ngControl, focusMonitor, elementRef);
        this._entity = null;
        this._availableEntities = null;
        this.entityControl = new FormControl();
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

    selected(event: MatAutocompleteSelectedEvent): void {
        this.entity = event.option.value as T;
    }

    private changeFromSubControls(): void {
        this.stateChanges.next();
        this.onChange(this.value);
    }

    protected abstract installAutoComplete(): Observable<T[]>;

    protected abstract displayWith(entity?: T): string;

    ngOnInit(): void {
        this._availableEntities = this.installAutoComplete();
    }
}

import {BaseInput} from '@app/input/base-input';
import {FormControl, NgControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {ElementRef, Input, OnInit, Optional, Self} from '@angular/core';
import {FocusMonitor} from '@angular/cdk/a11y';
import {MatAutocompleteSelectedEvent, MatChipInputEvent} from '@angular/material';
import {Entity} from '@app/entity/entity';

export abstract class MultiFieldsMultiEntityInput<T extends Entity> extends BaseInput<T[]> implements OnInit {

    protected static readonly EMPTY_ENTITIES: any[] = [];

    protected readonly entityControl: FormControl;

    protected _availableEntities: Observable<T[]>;
    protected _entities: T[];

    protected constructor(
        @Optional() @Self() ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>
    ) {
        super(ngControl, focusMonitor, elementRef);

        this._entities = MultiFieldsMultiEntityInput.EMPTY_ENTITIES as T[];
        this._availableEntities = null;
        this.entityControl = new FormControl();
    }

    remove(entity: T): void {

        const index = this._entities.indexOf(entity);

        if (index >= 0) {
            this._entities.splice(index, 1);
        }
    }

    selected(event: MatAutocompleteSelectedEvent): void {

        const entity = event.option.value as T;

        if (entity != null) {
            this.addEntity(entity);
        }

        this.entityControl.setValue(null);
    }

    protected addEntity(entity: T) {
        this._entities.push(entity);
        this.changeFromSubControls();
    }

    get entities(): T[] {
        return this._entities;
    }

    get empty(): boolean {
        return this._entities.length < 1;
    }

    get availableEntities(): Observable<T[]> {
        return this._availableEntities;
    }

    @Input()
    get value(): T[] {
        return this._entities;
    }

    set value(entities: T[]) {
        this._entities = entities;
        this.entityControl.setValue(null);
        this.stateChanges.next();
    }

    private changeFromSubControls(): void {
        this.stateChanges.next();
        this.onChange(this.value);
    }

    protected abstract installAutoComplete(): Observable<T[]>;

    protected abstract inputToEntity(value: any): T | null;

    protected abstract displayWith(entity?: T): string;

    ngOnInit(): void {
        this._availableEntities = this.installAutoComplete();
    }
}

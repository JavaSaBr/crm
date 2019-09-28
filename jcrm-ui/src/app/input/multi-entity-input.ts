import {BaseInput} from '@app/input/base-input';
import {FormControl, NgControl} from '@angular/forms';
import {Observable} from 'rxjs';
import {ElementRef, Input, OnInit, Optional, Self, ViewChild} from '@angular/core';
import {FocusMonitor} from '@angular/cdk/a11y';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {MatAutocomplete, MatAutocompleteSelectedEvent, MatChipInputEvent} from '@angular/material';
import {UniqEntity} from '@app/entity/uniq-entity';

export abstract class MultiEntityInput<T extends UniqEntity> extends BaseInput<T[]> implements OnInit {

    protected static readonly DEFAULT_SEPARATOR_KEYS_CODES: number[] = [ENTER, COMMA];
    protected static readonly EMPTY_ENTITIES: any[] = [];

    readonly entityControl: FormControl;

    protected _availableEntities: Observable<T[]>;
    protected _entities: T[];

    separatorKeysCodes: number[];

    @Input("selectable")
    selectable = false;
    @Input("removable")
    removable = true;
    @Input("addOnBlur")
    addOnBlur = false;

    @ViewChild('entityInput', {static: false})
    entityInput: ElementRef<HTMLInputElement>;

    @ViewChild('auto', {static: false})
    matAutocomplete: MatAutocomplete;

    protected constructor(
        @Optional() @Self() ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>
    ) {
        super(ngControl, focusMonitor, elementRef);

        this._entities = MultiEntityInput.EMPTY_ENTITIES as T[];
        this._availableEntities = null;
        this.separatorKeysCodes = this.getSeparatorKeysCodes();
        this.entityControl = new FormControl();
    }

    protected getSeparatorKeysCodes(): number[] {
        return MultiEntityInput.DEFAULT_SEPARATOR_KEYS_CODES;
    }

    removeEntity(entity: T): void {
        if (this.removeEntityInternal(entity)) {
            this.changeFromSubControls();
        }
    }

    removeEntityInternal(entity: T): boolean {

        const index = this._entities.indexOf(entity);

        if (index >= 0) {
            this._entities.splice(index, 1);
            return true;
        }

        return false;
    }

    add(event: MatChipInputEvent): void {

        if (this.matAutocomplete.isOpen) {
            return;
        }

        const input = event.input;
        const entity = this.inputToEntity(event.value);

        if (entity != null) {
            this.addEntity(entity);
        }

        // Reset the input value
        if (input) {
            input.value = '';
        }

        this.entityControl.setValue(null);
    }

    selected(event: MatAutocompleteSelectedEvent): void {

        const entity = event.option.value as T;

        if (entity != null) {
            this.addEntity(entity);
        }

        this.entityInput.nativeElement.value = '';
        this.entityControl.setValue(null);
    }

    private addEntity(entity: T) {
        if (this.addEntityInternal(entity)) {
            this.changeFromSubControls();
        }
    }

    private addEntityInternal(entity: T): boolean {

        const index = this._entities.findIndex(element => element.id == entity.id);

        if (index >= 0) {
            return false;
        }

        this._entities.push(entity);
        return true;
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

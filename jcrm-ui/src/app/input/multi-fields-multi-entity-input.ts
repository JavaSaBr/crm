import {BaseInput} from '@app/input/base-input';
import {FormControl, NgControl} from '@angular/forms';
import {ElementRef, Input, OnInit, Optional, Self} from '@angular/core';
import {FocusMonitor} from '@angular/cdk/a11y';
import {Entity} from '@app/entity/entity';

export abstract class MultiFieldsMultiEntityInput<T extends Entity> extends BaseInput<T[]> implements OnInit {

    protected _entities: T[];
    protected _entityToControl: Map<T, FormControl[]>;

    protected constructor(
        @Optional() @Self() ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>
    ) {
        super(ngControl, focusMonitor, elementRef);

        this._entities = [];
        this._entityToControl = new Map();
    }

    remove(entity: T): void {

        const index = this._entities.indexOf(entity);

        if (index < 0) {
            return;
        }

        this._entities.splice(index, 1);
        this._entityToControl.delete(entity);
    }

    protected addEntity(entity: T) {
        this._entities.push(entity);
        this._entityToControl.set(entity, this.createFormControls(entity));
        this.changeFromSubControls();
    }

    protected createFormControls(entity: T): FormControl[] {
        return [
            new FormControl()
        ];
    }

    protected findControlsFor(entity: T): FormControl[] {
        return this._entityToControl.get(entity);
    }

    get entities(): T[] {
        return this._entities;
    }

    get empty(): boolean {
        return this._entities.length < 1;
    }

    @Input()
    get value(): T[] {
        return this._entities;
    }

    set value(entities: T[]) {
        this._entities = entities;
        this.stateChanges.next();
    }

    private changeFromSubControls(): void {
        this.stateChanges.next();
        this.onChange(this.value);
    }

    ngOnInit(): void {
    }
}

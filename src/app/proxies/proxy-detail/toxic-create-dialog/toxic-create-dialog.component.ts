import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Toxic} from '../../../services/toxic';
import {MAT_DIALOG_DATA, MatDialogRef, MatSnackBar} from '@angular/material';
import {Proxy} from '../../../services/proxy';
import {ToxiproxyService} from '../../../services/toxiproxy.service';
import {ToxicData} from './toxic-data';
import {ToxicTypeConstants} from './toxic-type/toxic-type-constants';
import {ToxicType} from './toxic-type/toxic-type';

@Component({
  templateUrl: './toxic-create-dialog.component.html',
  styleUrls: ['./toxic-create-dialog.component.scss']
})
export class ToxicCreateDialogComponent implements OnInit {

  inProgress: boolean | false;
  selectedType: ToxicType;
  toxicForm: FormGroup;
  types: ToxicType[] = ToxicTypeConstants.getToxicTypes();
  proxy: Proxy;
  toxic: Toxic;
  isUpdate: boolean;

  constructor(private fb: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data: ToxicData,
              private proxyService: ToxiproxyService,
              private snackBar: MatSnackBar,
              private dialog: MatDialogRef<ToxicCreateDialogComponent>) {
  }

  ngOnInit() {
    this.proxy = this.data.proxy;
    this.toxic = this.data.toxic;
    this.isUpdate = this.toxic ? true : false;
    this.isUpdate ? this.createFormForUpdate() : this.createFormForNew();
  }

  createFormForUpdate() {
    this.toxicForm = this.fb.group({
      name: [{'value': this.toxic.name, 'disabled': true}, Validators.required],
      type: [{'value': this.toxic.type, 'disabled': true}, Validators.required],
      stream: [{'value': this.toxic.stream, 'disabled': true}, Validators.required],
      toxicity: [this.toxic.toxicity + '', Validators.required]
    });

    this.typeSelect(this.toxic.type);
  }

  createFormForNew() {
    this.toxicForm = this.fb.group({
      name: [{'value': '', 'disabled': true}],
      type: ['', Validators.required],
      stream: ['', Validators.required],
      toxicity: ['1.0', Validators.required]
    });
  }

  typeSelect(selectedToxicValue: string) {
    this.selectedType = ToxicTypeConstants.getToxicTypeByValue(selectedToxicValue);
    const attributeGroup = this.fb.group({});

    this.selectedType.attributes.forEach(attribute => {
      let value = this.toxic && this.toxic.attributes[attribute.value] ? this.toxic.attributes[attribute.value] : '';
      attributeGroup.addControl(attribute.value, new FormControl(value + '', Validators.required));
    });

    this.toxicForm.addControl('attributes', attributeGroup);
  }

  onSubmit() {
    this.inProgress = true;
    const toxic = this.createToxicFromFormData();
    this.isUpdate ? this.updateToxic(toxic) : this.createToxic(toxic);
  }

  createToxicFromFormData(): Toxic {
    const toxic = new Toxic();
    toxic.name = this.toxicForm.get('name').value;
    toxic.type = this.toxicForm.get('type').value;
    toxic.attributes = ToxicTypeConstants.convertFormAttributesToDataType(this.selectedType, this.toxicForm.get('attributes'));
    toxic.stream = this.toxicForm.get('stream').value;
    toxic.toxicity = Number(this.toxicForm.get('toxicity').value).valueOf();
    return toxic;
  }

  updateToxic(toxic: Toxic) {
    this.proxyService
      .updateToxic(this.proxy, toxic)
      .subscribe(
        () => {
          this.inProgress = false;
          this.dialog.close();
        },
        () => {
          this.inProgress = false;
          this.snackBar.open(
            'Unable to edit toxic.',
            'Close',
            {duration: 5000});
        }
      );
  }

  createToxic(toxic: Toxic) {
    this.proxyService
      .addToxic(this.proxy, toxic)
      .subscribe(
        () => {
          this.dialog.close();
        },
        () => {
          this.snackBar.open(
            'Unable to add toxic proxy.',
            'Close',
            {duration: 5000});
        },
        () => this.inProgress = false
      );
  }
}

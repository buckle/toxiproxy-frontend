import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Toxic} from '../../../services/toxic';
import {MAT_DIALOG_DATA, MatDialogRef, MatSnackBar} from '@angular/material';
import {Proxy} from '../../../services/proxy';
import {ToxiproxyService} from '../../../services/toxiproxy.service';
import {ToxicType} from './toxic-type';
import {ToxicTypeConstants} from './toxic-type-constants';
import {ToxicData} from './toxic-data';

@Component({
  selector: 'app-toxic-create-dialog',
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

    this.proxy = data.proxy;
    this.toxic = data.toxic;
    this.isUpdate = this.toxic ? true : false;
  }

  ngOnInit() {
    if(this.isUpdate) {
      this.toxicForm = this.fb.group({
        type: [{'value': this.toxic.type, 'disabled': true}, Validators.required],
        stream: [{'value': this.toxic.stream, 'disabled': true}, Validators.required],
        toxicity: [this.toxic.toxicity + '', Validators.required]
      });

      this.typeSelect(this.toxic.type, this.toxic);
    } else {
      this.toxicForm = this.fb.group({
        type: ['', Validators.required],
        stream: ['', Validators.required],
        toxicity: ['1.0', Validators.required]
      });
    }
  }

  typeSelect(selectedToxicValue: string, toxic: Toxic) {
    this.selectedType = ToxicTypeConstants.getToxicTypeByValue(selectedToxicValue);
    const attributeGroup = this.fb.group({});

    this.selectedType.attributes.forEach(attribute => {
      let value = '';
      if(toxic) {
        value = this.toxic.attributes[attribute.value] ? this.toxic.attributes[attribute.value] : '';
      }

      attributeGroup.addControl(attribute.value, new FormControl(value + '', Validators.required));
    });

    this.toxicForm.removeControl('attributes');
    this.toxicForm.addControl('attributes', attributeGroup);
  }

  onSubmit() {
    const type = this.toxicForm.get('type').value;
    const stream = this.toxicForm.get('stream').value;
    const toxicity = Number(this.toxicForm.get('toxicity').value).valueOf();
    const attributes = {};

    this.selectedType.attributes.forEach(attribute => {
      const attributeFormControl = this.toxicForm.get('attributes').get(attribute.value);
      let data;

      switch(attribute.dataType) {
        case 'number':
          data = Number(attributeFormControl.value).valueOf();
          break;
      }

      attributes[attribute.value] = data;
    });

    const toxic = new Toxic();
    toxic.name = this.isUpdate ? this.toxic.name : null;
    toxic.type = type;
    toxic.attributes = attributes;
    toxic.stream = stream;
    toxic.toxicity = toxicity;

    this.inProgress = true;

    if(this.isUpdate) {
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
              {duration: 8000});
          },
          () => this.inProgress = false
        );

    } else {
      this.proxyService
        .addToxic(this.proxy, toxic)
        .subscribe(
          () => {
            this.inProgress = false;
            this.dialog.close();
          },
          () => {
            this.inProgress = false;
            this.snackBar.open(
              'Unable to add toxic proxy.',
              'Close',
              {duration: 8000});
          },
          () => this.inProgress = false
        );
    }
  }
}

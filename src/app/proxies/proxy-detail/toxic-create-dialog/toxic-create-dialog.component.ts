import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Toxic} from '../../../services/toxic';
import {MAT_DIALOG_DATA, MatDialogRef, MatSnackBar} from '@angular/material';
import {Proxy} from '../../../services/proxy';
import {ToxiproxyService} from '../../../services/toxiproxy.service';
import {ToxicType} from './toxic-type';
import {ToxicTypeConstants} from './toxic-type-constants';

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

  constructor(private fb: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public proxy: Proxy,
              private proxyService: ToxiproxyService,
              private snackBar: MatSnackBar,
              private dialog: MatDialogRef<ToxicCreateDialogComponent>) {
  }

  ngOnInit() {
    this.toxicForm = this.fb.group({
      type: ['', Validators.required],
      stream: ['', Validators.required]
    });
  }

  typeSelect(selectedToxicValue) {
    this.selectedType = ToxicTypeConstants.getToxicTypeByValue(selectedToxicValue);
    const attributeGroup = this.fb.group({});

    this.selectedType.attributes.forEach(attribute => {
      attributeGroup.addControl(attribute.value, new FormControl('', Validators.required));
    });

    this.toxicForm.removeControl('attributes');
    this.toxicForm.addControl('attributes', attributeGroup);
  }

  onSubmit() {
    const type = this.toxicForm.get('type').value;
    const stream = this.toxicForm.get('stream').value;
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
    toxic.type = type;
    toxic.attributes = attributes;
    toxic.stream = stream;

    this.inProgress = true;
    this.proxyService
      .addToxic(this.proxy, toxic)
      .subscribe(
        value => {
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
        () => {
          this.inProgress = false;
        }
      );
  }
}

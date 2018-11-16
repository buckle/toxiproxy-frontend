import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-proxy-create-dialog',
  templateUrl: './proxy-create-dialog.component.html',
  styleUrls: ['./proxy-create-dialog.component.scss']
})
export class ProxyCreateDialogComponent implements OnInit {

  inProgress: boolean | false;
  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      name: ['', Validators.required]
    });
  }

  ngOnInit() {
  }

  onSubmit() {

  }
}

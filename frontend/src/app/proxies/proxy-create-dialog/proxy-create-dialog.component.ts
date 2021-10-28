import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ToxiproxyService} from '../../services/toxiproxy.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import {Proxy} from '../../services/proxy';

@Component({
  templateUrl: './proxy-create-dialog.component.html',
  styleUrls: ['./proxy-create-dialog.component.scss']
})
export class ProxyCreateDialogComponent implements OnInit {

  inProgress: boolean | false;
  form: FormGroup;
  isUpdate: boolean | false;

  constructor(private dialog: MatDialogRef<ProxyCreateDialogComponent>,
              private fb: FormBuilder,
              private proxyService: ToxiproxyService,
              private snackBar: MatSnackBar,
              @Inject(MAT_DIALOG_DATA) public proxy: Proxy | any) {
  }

  ngOnInit() {
    this.initForm(this.proxy);
  }

  initForm(proxy: Proxy) {
    if(proxy) {
      this.isUpdate = true;
      this.form = this.fb.group({
        name: [{'value': proxy.name, 'disabled': true}, Validators.required],
        listen: [proxy.listen, Validators.required],
        upstream: [proxy.upstream, Validators.required],
        enabled: proxy.enabled + ''
      });
    } else {
      this.isUpdate = false;
      this.form = this.fb.group({
        name: ['', Validators.required],
        listen: ['', Validators.required],
        upstream: ['', Validators.required],
        enabled: 'true'
      });
    }
  }

  onSubmit() {
    this.inProgress = true;
    const proxy = new Proxy();
    proxy.name = this.form.get('name').value;
    proxy.listen = this.form.get('listen').value;
    proxy.upstream = this.form.get('upstream').value;
    proxy.enabled = this.form.get('enabled').value == 'true';

    if(this.isUpdate) {
      this.proxyService
        .updateProxy(proxy)
        .subscribe(
          () => this.dialog.close(),
          () => {
            this.inProgress = false;
            this.snackBar.open(
              'Unable to update proxy.',
              'Close',
              {duration: 8000});
          },
          () => this.inProgress = false
        );
    } else {
      this.proxyService
        .createProxy(proxy)
        .subscribe(
          () => this.dialog.close(),
          () => {
            this.inProgress = false;
            this.snackBar.open(
              'Unable to create proxy. Proxy name and listen ports must be unique.',
              'Close',
              {duration: 8000});
          },
          () => this.inProgress = false
        );
    }
  }
}

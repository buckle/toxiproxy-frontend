const {writeFile} = require('fs');

const targetPath = `./src/environments/environment.prod.ts`;
const envConfigFile = `
export const environment = {
  production: true,
  toxiproxyHost: "${process.env.TOXIPROXY_HOST != undefined ? process.env.TOXIPROXY_HOST : 'http://localhost:8474'}"
};
`;

writeFile(targetPath, envConfigFile, function(err) {
  if(err) {
    console.log(err);
  }

  console.log(`Output generated at ${targetPath}`);
});

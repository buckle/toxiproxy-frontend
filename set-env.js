const {writeFile} = require('fs');

const targetPath = `./src/environments/environment.prod.ts`;
const envConfigFile = `
export const environment = {
  production: true,
  toxiproxyHost: "${process.env.toxiproxyHost != undefined ? process.env.toxiproxyHost : 'http://localhost:8474'}"
};
`;

writeFile(targetPath, envConfigFile, function(err) {
  if(err) {
    console.log(err);
  }

  console.log(`Output generated at ${targetPath}`);
});

Void call(Closure body) {
     registryCreds = getRegistryCreds()
     docker.withRegistry(registryCreds["url"], creds["userPass"]) {
         body()
     }
 }

Map getRegistryCreds() {
   withCredentials([usernamePassword(
       credentialsId: 'dockerHub', //change this to fit theregistry  credential Id in your jenkins instance
       usernameVariable: 'USERNAME',
       passwordVariable: 'PASSWORD')
   ]) {
     return [
       userPass: "dockerHub",
       url: "https://index.docker.io/v1/"
     ]
   }
 }

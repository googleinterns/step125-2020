

var firebaseConfig = {
    apiKey: "AIzaSyDa2o12S4k-ILczNuf-WpmeY2zIFoUFxAQ",
    authDomain: "maptivist-step-2020.firebaseapp.com",
    databaseURL: "https://maptivist-step-2020.firebaseio.com",
    projectId: "maptivist-step-2020",
    storageBucket: "maptivist-step-2020.appspot.com",
    messagingSenderId: "1032109305013",
    appId: "1:1032109305013:web:bef8b67db0d5ae091422fc",
    measurementId: "G-E38S0D5C9B"
};
    firebase.initializeApp(firebaseConfig);
    firebase.analytics();


function signUp() {
    var email = document.getElementById("email");
    var password = document.getElementById("pass");
    const promise = firebase.auth().createUserWithEmailAndPassword(email.value, password.value);
    promise.catch(e => alert(e.message));
    alert("Signed Up");
  }
  
function signIn(){
    var email = document.getElementById("email");
    var password = document.getElementById("password");
    const promise = auth.signInWithEmailAndPassword(email.value, password.value);
    promise.catch(e => alert(e.message));
    alert("Signed In" + email.value);
 }

function signOut(){
    firebase.auth().signOut();
    alert("Signed Out");
}

 firebase.auth().onAuthStateChanged(function(user){
    if(user){
        var email = user.email;
        alert("Active User " + email);
    }
    else{
        alert("No Active User");
    }
 });
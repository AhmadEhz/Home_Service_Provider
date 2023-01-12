function submitForm() {
    var xhr = new XMLHttpRequest();
    const paymentForm = {
        cardNumber: document.getElementById("card-number").value,
        cvv2: document.getElementById("cvv2").value,
        expireDate: document.getElementById("expire-date").value,
        captcha: grecaptcha.getResponse()
    };
xhr.open("POST", "http://localhost:8080/customer/payment", true);
xhr.setRequestHeader("Content-Type", "application/json");
var paymentJson = JSON.stringify(paymentForm);
console.log(paymentJson);
xhr.send(paymentJson);
}
function submitForm() {
    var xhr = new XMLHttpRequest();
    var forms = document.getElementById("forms");
    const form = {
        cardNumber: document.getElementById("card-number").value,
        cvv2: document.getElementById("cvv2").value,
        expireDate: document.getElementById("expire-date").value,
        captcha: grecaptcha.getResponse()

    };
    // if (xhr.readyState === 4 && xhr.status === 200) {

    //     // Print received data from server
    //     result.innerHTML = this.responseText;

    // }

xhr.open("POST", "http://localhost:8080/customer/payment", true);
xhr.setRequestHeader("Content-Type", "application/json");
xhr.setRequestHeader("Access-Control-Allow-Origin","*");
var data = JSON.stringify(form);
console.log(data);
xhr.send(data);
forms.submit();
location.href = "http://google.com";
// return false;
}
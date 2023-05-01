let checkout_form = $("#checkout_form");


function calculateMatch(resultArray) {
    let form_data = checkout_form.serialize();
    let form_data_array = form_data.split("&");
    let SAT = form_data_array[0];
    let cost = form_data_array[1];
    let genre = form_data_array[2];

    for (let i = 0; i < resultArray.length; i++) {
        let innerArray = JSON.parse(resultArray[i]);
        console.log(innerArray);
    }
    console.log(resultArray);
    console.log(SAT);
    console.log(cost);
    console.log(genre);
}
function submitCheckoutForm(formSubmitEvent) {
    console.log("submit user checkout form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax("api/cart", {
        method: "GET",
        // Serialize the login form to the data sent by POST request
        success: resultDataString => {
            let resultDataJson = JSON.parse(resultDataString);
            calculateMatch(resultDataJson["previousItems"]);
        }
    });
}
checkout_form.submit(submitCheckoutForm);
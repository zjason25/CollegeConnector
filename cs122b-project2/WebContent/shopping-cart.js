/**
 * Handle the data returned by IndexServlet
 * @param resultDataString jsonObject, consists of session info
 */
function handleSessionData(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);
    // show wishlist information
    handleCartArray(resultDataJson["previousItems"]);
}

/**
 * Handle the items in wishlist
 * @param resultArray jsonObject, needs to be parsed to html
 */
function handleCartArray(resultArray) {
    console.log(resultArray);
    let wishlist_items = $("#wishlist-items");
    // change it to html list

    for (let i = 0; i < resultArray.length; i++) {
        let record = JSON.parse(resultArray[i])

        // each item will be in a bullet point
        let res = "<tr>";
        res += "<th><a href=\"single-school.html?id=" + record["school_id"] + '">' + record["name"] + "</th>";
        res += "<th>" + record["genre"] + "</th>";
        res += "<th><a href=\"single-location.html?id=" + record["location_id"] + '">' + record["state"] + "</th>";
        res += "<th><select name=\"page\" id=\"page\">\n" +
            "        <option value=1>1</option>\n" +
            "        <option value=2>2</option>\n" +
            "        <option value=3>3</option>\n" +
            "        <option value=4>4</option>\n" +
            "        <option value=5>5</option>\n" +
            "    </select></th>";
        res += "<th><button onclick='remove_school(" + '"' + record["name"] + '"' + ", \"true\")'>Remove</button></th>";
        res += "</tr>";
        console.log(res);

        // display resulting html to appropriate wishlist-items field in shopping.html
        wishlist_items.append(res);
    }
}

function remove_school(school_name, remove) {
    window.alert(school_name + " removed from wishlist!");
    $.ajax("api/cart", {
            method: "POST",
            data: {name: school_name, remove: remove},
            success: resultData => {
                handleCartArray(resultData)
            }
    });
    window.location.replace("shopping-cart.html");
}


$.ajax("api/cart", {
    method: "GET",
    success: handleSessionData
});


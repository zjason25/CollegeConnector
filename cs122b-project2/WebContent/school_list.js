/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    // System.out.println(target);
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function handleSchoolResult(resultData) {
    console.log("handleSchoolResult: populating School table from resultData");

    // Populate the School table
    // Find the empty table body by id "School_table_body"
    let schoolTableBodyElement = jQuery("#school_table_body");

    // Iterate through resultData, no more than 10 entries
    for (let i = 0; i < Math.min(20, resultData.length); i++) {

        // Concatenate the html tags with resultData jsonObject
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-school.html with id passed with GET url parameter
            '<a href="single-school.html?id=' + resultData[i]['school_id'] + '">'
            + resultData[i]["school_name"] +     // display school_name for the link text
            '</a>' +
            "</th>";

        rowHTML += "<th>" + resultData[i]["school_rating"] + "</th>";

        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-school.html with id passed with GET url parameter
            '<a href="single-location.html?id=' + resultData[i]['location_id']+ '">'
            + resultData[i]["school_city"] +", "+ resultData[i]["school_state"] +    // display school_name for the link text
            '</a>' +
            "</th>";
        rowHTML += "<th>Website: <a href='" + resultData[0]["link_to_website"] + "'>" + resultData[0]["link_to_website"] + "</a></th>"
        rowHTML += "<th>" + resultData[i]["safety"] + "</th>";
        rowHTML += "<th>" + resultData[i]["telephone"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        schoolTableBodyElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
let schoolName = getParameterByName("school");
let locationName = getParameterByName("location");
let otherName = getParameterByName("other");
let orderName = getParameterByName("order");
let genreName = getParameterByName("genre");


// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/school_list?school=" + schoolName+"&location=" + locationName+"&other="+ otherName+"&order="+ orderName + "&genre="+ genreName,// Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleSchoolResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});
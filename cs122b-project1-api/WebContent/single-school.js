/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it know which id to look for
 *      2. Use jQuery to talk to backend API to get the json data.
 *      3. Populate the data to correct html elements.
 */


/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleResult(resultData) {

    console.log("handleResult: populating school info from resultData");

    // populate the star info h3
    // find the empty h3 body by id "star_info"
    let schoolNameElement = jQuery("#school_name");
    schoolNameElement.append("<p>School Name: " + resultData[0]["school_name"] + "</p>");
    let schoolInfoElement = jQuery("#school_info");

    // append two html <p> created to the h3 body, which will refresh the page
    schoolInfoElement.append(
        "<p>" +
        // Add a link to single-school.html with id passed with GET url parameter
        '<a href="index.html">'
        + "Back to School List"+
        '</a>' +
        "</p>"
        + "<p>Genre: " + resultData[0]["genre"] + "</p>"
        + "<p>Description: " + resultData[0]["school_dis"] + "</p>"
        + "<p>Net Cost: " + "$"+resultData[0]["net_cost"]+ " per year" + "</p>"
        + "<p>SAT Range: " + resultData[0]["upper_SAT"]+"~"+resultData[0]["lower_SAT"] + "</p>"
        + "<p>Website: <a href='" + resultData[0]["link_to_website"] + "'>" + resultData[0]["link_to_website"] + "</a></p>"
        + "<p>Location Information" + "</p>");

    console.log("handleResult: populating location table from resultData");

    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let locationTableBodyElement = jQuery("#location_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < Math.min(1, resultData.length); i++) {
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-school.html with id passed with GET url parameter
            '<a href="single-location.html?id=' + resultData[i]['location_id'] + '">'
            + resultData[i]["city"] +     // display school_name for the link text
            '</a>' +
            "</th>";
        rowHTML += "<th>" +  resultData[0]["address"] + "</th>";
        rowHTML += "<th>" + resultData[i]["state"] + "</th>";
        rowHTML += "<th>" + resultData[i]["safety_level"] +" out of 10"+ "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        locationTableBodyElement.append(rowHTML);
    }
    document.getElementById("school_image").src = resultData[0]["link_to_image"];

}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Get id from URL
let school_id = getParameterByName("id");

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/single-school?id=" + school_id, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});

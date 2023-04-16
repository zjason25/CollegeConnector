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
        rowHTML += "<th>" + resultData[i]["school_dis"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        schoolTableBodyElement.append(rowHTML);
    }
}


/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */

// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/school", // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleSchoolResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});
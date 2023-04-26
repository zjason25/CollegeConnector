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
let sort_form = $("#sort_form");
let page_form = $("#page_form");

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

    let schoolTableBodyElement = jQuery("#school_table_body");

    resultPerpage = resultData[0]["pagenum"]
    whichpage = resultData[0]["whichpage"]

    console.log("resultPerpage: " + resultPerpage);
    console.log("whichpage: " + whichpage);
    console.log("resultData length: " + resultData.length);
    console.log(Math.min( resultPerpage, resultData.length)+1);


    // Iterate through resultData, no more than 10 entries
    for (let i = 1; i < Math.min( resultPerpage, resultData.length)+1; i++) {
        // Concatenate the html tags with resultData jsonObject
        console.log(resultData[i]["school_name"]);
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

        rowHTML +=
            "<th>" +
            // Add a link to single-school.html with id passed with GET url parameter
            '<a href="single-location.html?id=' + resultData[i]['location_id']+ '">'
            + resultData[i]["school_city"] +", "+ resultData[i]["school_state"] +    // display school_name for the link text
            '</a>' +
            "</th>";
        rowHTML += "<th>Website: <a href='" + resultData[i]["link_to_website"] + "'>" + resultData[i]["link_to_website"] + "</a></th>"
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
console.log("1");

let schoolName = getParameterByName("school");
let locationName = getParameterByName("location");
let otherName = getParameterByName("other");
let orderName = getParameterByName("order");
let genreName = getParameterByName("genre");
let pagenum = getParameterByName("pagenum");
console.log("2");

let whichpage = getParameterByName("whichpage");
console.log("3");
console.log(whichpage);


// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/school_list?school=" + schoolName+"&location=" + locationName+"&other="+ otherName+"&order="+ orderName + "&genre="+ genreName+ "&pagenum="+ pagenum+ "&whichpage="+ whichpage,// Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleSchoolResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

function submitSortForm(event) {
    event.preventDefault();
    const sortValue = $('#sort').val();
    window.location.replace('school_list.html?school=' + schoolName+"&location=" + locationName+"&other="+ otherName+"&order="+ sortValue + "&genre="+ genreName+ "&pagenum="+ pagenum+ "&whichpage="+ whichpage);
}

function submitPageForm(event) {
    event.preventDefault();
    const pageValue = $('#page').val();
    window.location.replace( 'school_list.html?school=' + schoolName+"&location=" + locationName+"&other="+ otherName+"&order="+ orderName + "&genre="+ genreName+ "&pagenum="+pageValue+ "&whichpage="+ whichpage);// Setting request url, which is mapped by StarsServlet in Stars.java
}

sort_form.submit(submitSortForm);
page_form.submit(submitPageForm);


const previousBtn = document.querySelector("#previousBtn");
const nextBtn = document.querySelector("#nextBtn");
previousBtn.addEventListener("click", function(event) {
    // Handle previous button click
    console.log("Previous button clicked");
    if (parseInt(whichpage)-1>0){
        whichpage = (parseInt(whichpage)-1).toString();
    }
    else{
        whichpage = "0";
    }
    window.location.replace( 'school_list.html?school=' + schoolName+"&location=" + locationName+"&other="+ otherName+"&order="+ orderName + "&genre="+ genreName+ "&pagenum="+pagenum+ "&whichpage="+ whichpage);// Setting request url, which is mapped by StarsServlet in Stars.java
});
nextBtn.addEventListener("click", function(event) {
    // Handle next button click
    console.log("Next button clicked");
    whichpage = (parseInt(whichpage)+1).toString();
    window.location.replace( 'school_list.html?school=' + schoolName+"&location=" + locationName+"&other="+ otherName+"&order="+ orderName + "&genre="+ genreName+ "&pagenum="+pagenum+ "&whichpage="+ whichpage);// Setting request url, which is mapped by StarsServlet in Stars.java
});
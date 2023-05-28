let search_form = $("#search_form");
let browse_form2 = $("#browse_form2");
let browse_form1 = $("#browse_form1");
const select = document.getElementById('browse_way1');

const options = ['Public University', 'Private University', 'Liberal Arts College', 'Technical College', 'Trade School', 'Alchemy School', 'Arts School', 'Online', 'Institution', 'Coding Bootcamp', 'Reading Club', 'Writers College', 'Training Facility', 'Cult', 'Military', 'Waldorf', 'Law School', 'Medical School', 'Business School', 'Community College', 'Vocational School', 'Music College', 'Mime School'];

for (let i = 0; i < options.length; i++) {
    const option = document.createElement('option');
    option.value = options[i];
    option.textContent = options[i];
    select.appendChild(option);
}

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleSearchResult(resultDataString) {
    let resultDataJson = JSON.parse(JSON.stringify(resultDataString));

    console.log("handle search response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If search succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        if (resultDataJson['school'].length > 0) {
            localStorage.setItem("lstLink", JSON.stringify('school_list.html?school=' + resultDataJson['school'] + "&location=" + resultDataJson['location'] + "&other=" + resultDataJson['other'] + "&order=" + resultDataJson['order'] + "&genre=" + resultDataJson['genre'] + "&pagenum=20" + "&whichpage=0" + "&fulltext=false"));
            window.location.replace('school_list.html?school=' + resultDataJson['school'] + "&location=" + resultDataJson['location'] + "&other=" + resultDataJson['other'] + "&order=" + resultDataJson['order'] + "&genre=" + resultDataJson['genre'] + "&pagenum=20" + "&whichpage=0" + "&fulltext=false");
        } else {
            localStorage.setItem("lstLink", JSON.stringify('school_list.html?school=' + resultDataJson['fulltext_school'] + "&location=" + resultDataJson['location'] + "&other=" + resultDataJson['other'] + "&order=" + resultDataJson['order'] + "&genre=" + resultDataJson['genre'] + "&pagenum=20" + "&whichpage=0" + "&fulltext=true"));
            window.location.replace('school_list.html?school=' + resultDataJson['fulltext_school'] + "&location=" + resultDataJson['location'] + "&other=" + resultDataJson['other'] + "&order=" + resultDataJson['order'] + "&genre=" + resultDataJson['genre'] + "&pagenum=20" + "&whichpage=0" + "&fulltext=true");
        }
    }
    else {
        // If search fails, the web page will display
        // error messages on <div> with id "search_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#search_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitSearchForm(formSubmitEvent) {
    console.log("submit search form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/search", {
            method: "POST",
            // Serialize the search form to the data sent by POST request
            data: search_form.serialize(),
            success: handleSearchResult
        }
    );
}

function submitBrowseForm(event) {
    event.preventDefault();

    // Get the selected value from the dropdown menu
    const browseWayValue2 = $('#browse_way2').val();
    const browseWayValue1 = $('#browse_way1').val();

    // Construct the search query using the form data and the dropdown value
    const searchQuery = {
        browse_way2: browseWayValue2, browse_way1: browseWayValue1
        // Add other form fields here
    };
    // Send the search query to the server using AJAX
    $.ajax({
        type: 'POST',
        url: 'api/search',
        data: searchQuery,
        success: handleSearchResult,
        error: function(jqXHR, textStatus, errorThrown) {
            // Handle errors
            console.log("Browse request failed: " + textStatus + ", " + errorThrown);
        }
    });
}


function handleLookup(query, doneCallback) {
    console.log("autocomplete initiated")

    // TODO: if you want to check past query results first, you can do it here
    let cached_suggestions = sessionStorage.getItem(query)

    if (cached_suggestions != null) {
        console.log("using cached suggestion")
        console.log("cached suggestion list: \n" + cached_suggestions)
        let jsonData = JSON.parse(cached_suggestions);
        doneCallback( { suggestions: jsonData } );
        return;
    }
    // sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
    // with the query data
    console.log("sending AJAX request to backend Java Servlet")
    jQuery.ajax({
        "method": "GET",
        // generate the request url from the query.
        // escape the query string to avoid errors caused by special characters
        "url": "api/school_list?school=" + escape(query) + "&location=null&other=null&order=null&genre=null" + "&pagenum=" + "20" + "&whichpage=0" + "&fulltext=true" + "&autocomplete=true",
        "success": function(data) {
            // pass the data, query, and doneCallback function into the success handler
            handleLookupAjaxSuccess(data, query, doneCallback)
        },
        "error": function(errorData) {
            console.log("lookup ajax error")
            console.log(errorData)
        }
    })
}

/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 *
 * data is the JSON data string you get from your Java Servlet
 *
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
    console.log("lookup ajax successful")

    // parse the string into JSON
    // var jsonData = JSON.parse(data);
    data.shift()
    let suggestion_list = JSON.stringify(data)
    console.log("suggestion list from server: \n" + suggestion_list)

    // TODO: if you want to cache the result into a global variable you can do it here
    sessionStorage.setItem(query, suggestion_list);

    // call the callback function provided by the autocomplete library
    // add "{suggestions: jsonData}" to satisfy the library response format according to
    //   the "Response Format" section in documentation
    // default dontCallback function provided by the autocomplete library
    doneCallback( { suggestions: data } );
}


/*
 * This function is the select suggestion handler function.
 * When a suggestion is selected, this function is called by the library.
 *
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
    // TODO: jump to the specific result page based on the selected suggestion

    console.log("you select " + suggestion["value"] + " with ID " + suggestion["data"])
    localStorage.setItem("lstLink", JSON.stringify('single-school.html?id=' + suggestion["data"] + "&location=null" + "&other=null" + "&order=null" + "&genre=null" + "&pagenum=20" + "&whichpage=0"));
    window.location.replace('single-school.html?id=' + suggestion["data"] + "&location=null" + "&other=null" + "&order=null" + "&genre=null" + "&pagenum=20" + "&whichpage=0");
}

/*
 * This statement binds the autocomplete library with the input box element and
 *   sets necessary parameters of the library.
 *
 * The library documentation can be find here:
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 *
 */
// $('#autocomplete') is to find element by the ID "autocomplete"
// gets hold of identifier "autocomplete in index.html. Binds that field to autocomplete
$('#autocomplete').autocomplete({
    // documentation of the lookup function can be found under the "Custom lookup function" section
    // matches different event to functions
    // doneCallback: a default function provided by jQuery

    lookup: function (query, doneCallback) {
        handleLookup(query, doneCallback)
    },
    // autocomplete returns a suggestion, which is to be passed to onSelect:
    onSelect: function(suggestion) {
        handleSelectSuggestion(suggestion)
    },
    // set delay time. Give the users time to finish typing before searching; saves search resources
    // a delay for each event before a search is triggered
    deferRequestBy: 300,
    minChars: 3
    // there are some other parameters that you might want to use to satisfy all the requirements
});

//
// // bind pressing enter key to a handler function
// $('#autocomplete').keypress(function(event) {
//     // keyCode 13 is the enter key
//     if (event.keyCode == 13) {
//         // pass the value of the input box to the handler function
//         handleNormalSearch($('#autocomplete').val())
//     }
// })

// Bind the submit action of the form to a handler function
search_form.submit(submitSearchForm);
browse_form1.submit(submitBrowseForm);
browse_form2.submit(submitBrowseForm);


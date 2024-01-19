window.onload = init;


function init() {
    setPageSettings();
    caretClass();
}



function setPageSettings() {   
    var currentUrl = window.location.pathname;
    // Remove any existing "active" class
    $('.navbar-nav .nav-item').removeClass('active');
    // Set the "active" class based on the current URL
    $('.navbar-nav .nav-item').each(function () {
        var navLink = $(this).find('.nav-link');
        var linkUrl = navLink.attr('href');
        // Check if the current URL starts with the link's URL
        if (currentUrl.startsWith(linkUrl)) {
            // Set the dropdown and its sub-menu as active
            $(this).addClass('active');
            navLink.addClass('active');
            // For dropdowns with sub-menus, set the parent dropdown and its sub-menu as active 
        } else if (currentUrl == "/" || currentUrl =="/pendingFilter") {
            let li = document.getElementById("navbarDropdownVisits")
            li.className = "nav-link active";
        }
    });
    // Additional action for locations including "checklist"
    if (currentUrl.includes("checklist")) {
        setupEventListeners();
    } else if (currentUrl.includes("bulk")) {
        form = document.getElementById('createVisits')
        form.addEventListener("submit", bulkFormSubmit)
    }

   

}

function caretClass() {
    $(document).ready(function () {
        // Dropdown Event Handlers
        $('.dropdown').on('shown.bs.dropdown', function () {
            $(this).find('.caret').removeClass('caret-down').addClass('caret-up');
        });

        $('.dropdown').on('hidden.bs.dropdown', function () {
            $(this).find('.caret').removeClass('caret-up').addClass('caret-down');
        });

    });
}



function cancelAppointment(id) {
    if (window.confirm("Are you sure you want to cancel this appointment?")) {
        let xhttp = new XMLHttpRequest();
        xhttp.open("POST", "/visitor/cancel", true);
        xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
        xhttp.send("id=" + id);
        window.setTimeout(reload, 500)
        // xhttp.close();
    }
}
function reload() {
    window.location.assign("/")
}




function editLocationSetStatus() {

    let status = document.getElementById('currentStatus').value;
    let select = document.getElementById("isActive");
    if (status == "Active") {
        select.innerHTML = `\n<option value=\"true\" selected=\"selected\">Active</option>\n<option value=\"false\">Inactive</option>\n`
    } else {
        select.innerHTML = `\n<option value=\"true\" >Active</option>\n<option value=\"false\" selected=\"selected\">Inactive</option>\n`
    }
}

    function setupEventListeners() {
    let listSize = Number(document.getElementById("numberOfLogs").innerHTML);
    let elementTypes = ["lg", "sm"];

    for (let j = 0; j < elementTypes.length; j++) {
        for (let i = 1; i <= listSize; i++) {
            let visitLog = document.getElementById(elementTypes[j] + "-visitLog" + i);
            let visitLogId = document.getElementById(elementTypes[j] + "-visitId" + i);
            // Attach focusout event listener
            visitLog.addEventListener("focusout", function () {
                handleFocusOut(visitLogId.value, visitLog.value);
            });
        }
    }
}

async function handleFocusOut(visitLogId, visitLogValue) {
    let url = "/visitingroom/checklist/table";
    let sendObject = {
        visitLogId: visitLogId,
        table: visitLogValue
    };

    try {
        let response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-type": "application/json"
            },
            body: JSON.stringify(sendObject)
        });

        if (!response.ok) {
            throw new Error("Network response was not ok");
        }
    } catch (error) {
        console.error("Fetch error:", error);
    }
}

function warning() {
    window.alert("Please allow 1 - 2 business days for a staff member to review your visit. \n\nMake sure to log back in to check appointment status and comments.")
}


function bulkFormSubmit(form) {

    let submitButton = document.getElementById('submit');
    submitButton.disabled = true;
    submitButton.innerHTML = "Processing"
    
}

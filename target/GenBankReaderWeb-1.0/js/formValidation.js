/* 
 * Copyright (c) 2015 Wout van Helvoirt [wout.van.helvoirt@gmail.com].
 * All rights reserved.
 */

$(function() {
    
    /* When upload file button toggled / pattern button, submit button show/hidden, enabled/disabled. */
    $('#button1, #button3').on('change',function(){
        var selection = $(this).val();
        if (selection !== "") {
            $("#button4").attr("disabled", false).slideDown(100);
        } else {
            $("#button4").attr("disabled", true).slideUp(100);
        }
    });

    /* When select option button toggled:
       Summary selected, enable/show submit
       Other, disable sumbit and show pattern input. */
    $('#button2').on('change',function(){
        var selection = $(this).val();
        if (selection !== "" && selection !== "Summary") {
            $("#button4").attr("disabled", true).slideUp(100);
            $("#button3").slideDown(100);
        } else if (selection === "Summary") {
            $("#button3").slideUp(100).val("");
            $("#button4").attr("disabled", false).slideDown(100);
        } else {
            $("#button3").val("");
            $("#button3, #button4").slideUp(100);
        }
    });

    /* Sumbit button clicked, change text and disable button to prevent multiple upload. */
    $("form").submit(function() {
        $("#button4")
            .val("Please wait while form is being submited...")
            .removeClass("buttonInactive")
            .addClass("buttonActive")
            .attr("disabled", true);
        return true;
    });
});
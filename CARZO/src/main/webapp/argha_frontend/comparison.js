document.addEventListener("DOMContentLoaded", function () {

    console.log("Carzo comparison.js loaded");


    const checkboxes =
        document.querySelectorAll(
            'input[name="carIds"]'
        );


    const selectedCount =
        document.getElementById(
            "selectedCount"
        );


    const compareButton =
        document.getElementById(
            "compareButton"
        );


    const selectAllButton =
        document.getElementById(
            "selectAllButton"
        );


    const clearSelectionButton =
        document.getElementById(
            "clearSelectionButton"
        );


    const compareForm =
        document.getElementById(
            "compareForm"
        );



    /* =========================
       UPDATE SELECTED COUNT
       ========================= */

    function updateSelection() {

        let count = 0;


        checkboxes.forEach(function (checkbox) {

            if (checkbox.checked) {

                count++;

            }

        });


        if (selectedCount) {

            selectedCount.textContent =
                count;

        }


        if (compareButton) {

            if (count >= 2 &&
                count <= 4) {

                compareButton.disabled =
                    false;

            } else {

                compareButton.disabled =
                    true;

            }

        }

    }



    /* =========================
       CHECKBOX CHANGE
       ========================= */

    checkboxes.forEach(function (checkbox) {

        checkbox.addEventListener(
            "change",
            function () {

                let count = 0;


                checkboxes.forEach(
                    function (item) {

                        if (item.checked) {

                            count++;

                        }

                    }
                );


                if (count > 4) {

                    this.checked = false;

                    alert(
                        "You can select maximum 4 cars."
                    );

                }


                updateSelection();

            }
        );

    });



    /* =========================
       SELECT FIRST 4
       ========================= */

    if (selectAllButton) {

        selectAllButton.addEventListener(
            "click",
            function () {

                let count = 0;


                checkboxes.forEach(
                    function (checkbox) {

                        if (count < 4) {

                            checkbox.checked =
                                true;

                            count++;

                        } else {

                            checkbox.checked =
                                false;

                        }

                    }
                );


                updateSelection();

            }
        );

    }



    /* =========================
       CLEAR SELECTION
       ========================= */

    if (clearSelectionButton) {

        clearSelectionButton.addEventListener(
            "click",
            function () {

                checkboxes.forEach(
                    function (checkbox) {

                        checkbox.checked =
                            false;

                    }
                );


                updateSelection();

            }
        );

    }



    /* =========================
       COMPARE FORM VALIDATION
       ========================= */

    if (compareForm) {

        compareForm.addEventListener(
            "submit",
            function (event) {

                let count = 0;


                checkboxes.forEach(
                    function (checkbox) {

                        if (checkbox.checked) {

                            count++;

                        }

                    }
                );


                if (count < 2) {

                    event.preventDefault();

                    alert(
                        "Please select at least 2 cars to compare."
                    );

                    return;

                }


                if (count > 4) {

                    event.preventDefault();

                    alert(
                        "You can compare maximum 4 cars."
                    );

                    return;

                }

            }
        );

    }



    /* =========================
       INITIAL COUNT
       ========================= */

    updateSelection();

});
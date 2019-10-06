
var global = this;
var totalSlotsLoaded = 0;
var numDollars = 0;
var numQuarters = 0;
var numDimes = 0;
var numNickels = 0;
var numPennies = 0;
var slotItemMap = new Map();
var purchaseComplete = false;


var currentItemId;
var url = "http://tsg-random-things.herokuapp.com/";

function refreshMoney() {
    let amountOfMoney = 0;
    amountOfMoney += numDollars;
    amountOfMoney += numQuarters/4;
    amountOfMoney += numDimes/10;
    amountOfMoney += numNickels/20;
    amountOfMoney += numPennies/100;

    $('#money-text').attr("value", amountOfMoney.toFixed(2));
}

function changeInString(quarters,dimes,nickles,pennies) {
    let changeArray = [];

    if (numQuarters > 0) {
        changeArray.push(numQuarters.toString() + " Quarters");
    }
    if (numDimes > 0) {
        changeArray.push(numDimes.toString() + " Dimes");
    }
    if (numNickels > 0) {
        changeArray.push(numNickels.toString() + " Nickels");
    }
    if (numPennies > 0) {
        changeArray.push(numPennies.toString() + " Pennies");
    }

    let changeString = changeArray.join(", ");
    if (numQuarters == 0 && numDimes == 0 && numNickels == 0 && numPennies == 0) {
        changeString = "No change"
    }

    return changeString;
}

function clearInput() {
    $('#item-text').attr("value", "");
    $('#message-text').val( "");
    $('#money-text').attr("value", "");
    $('#change-text').val("");
    numDollars = 0;
    numQuarters = 0;
    numDimes = 0;
    numNickels = 0;
    purchaseComplete = false;
}

function completePurchase() {
    purchaseComplete = true;
}

$(document).ready(function () {
    $('#khmer').click(function(event) {
        $('#title-text').text('ម៉ាស៊ីនលក់។');
        $('#message-header').text('ព័ត៌មាន');
        $('#message-text').text('ព័ត៌មាន');
        $('#make-purchase').text('ធាតុទិញ។');
        $('#dollar').text('បញ្ចូលដុល្លារ។');
        $('#quarter').text('បញ្ចូលត្រីមាស។');
        $('#nickel').text('បញ្ចូលត្រីមាស។');
        $('#dime').text('បញ្ចូលផេនី។');
        $('#item-colon').text('ធាតុ៖');
        $('#total-money-in').text('ប្រាក់សរុបក្នុង');
        $('#change-return').text('យកលុយមកវិញ។');
        $('#change-header').text('យកលុយមកវិញ។');
        $('#change-text').text('យកលុយមកវិញ។');
        $('.quantity-remaining').text('បរិមាណនៅសល់។');
    });
    $('#english').click(function(event) {
        location.reload()
    });

    loadItems();
    $('#slot1').click(function(event){
        currentItemId = slotItemMap.get(1);
        $('#item-text').attr("value", currentItemId);
    });
    $('#slot2').click(function(event){
        currentItemId = slotItemMap.get(2);
        $('#item-text').attr("value", currentItemId);
    });
    $('#slot3').click(function(event){
        currentItemId = slotItemMap.get(3);
        $('#item-text').attr("value", currentItemId);
    });
    $('#slot4').click(function(event){
        currentItemId = slotItemMap.get(4);
        $('#item-text').attr("value", currentItemId);
    });
    $('#slot5').click(function(event){
        currentItemId = slotItemMap.get(5);
        $('#item-text').attr("value", currentItemId);
    });
    $('#slot6').click(function(event){
        currentItemId = slotItemMap.get(6);
        $('#item-text').attr("value", currentItemId);
    });
    $('#slot7').click(function(event){
        currentItemId = slotItemMap.get(7);
        $('#item-text').attr("value", currentItemId);
    });
    $('#slot8').click(function(event){
        currentItemId = slotItemMap.get(8);
        $('#item-text').attr("value", currentItemId);
    });
    $('#slot9').click(function(event){
        currentItemId = slotItemMap.get(9);
        $('#item-text').attr("value", currentItemId);
    });
    $('#slot10').click(function(event){
        currentItemId = slotItemMap.get(10);
        $('#item-text').attr("value", currentItemId);
    });
    $('#slot11').click(function(event){
        currentItemId = slotItemMap.get(11);
        $('#item-text').attr("value", currentItemId);
    });
    $('#slot12').click(function(event){
        currentItemId = slotItemMap.get(12);
        $('#item-text').attr("value", currentItemId);
    });
    $('#make-purchase').click(function(event) {
        let currentMoney = 0;
        currentMoney += numDollars;
        currentMoney += numQuarters / 4;
        currentMoney += numDimes / 10;
        currentMoney += numNickels / 20;

        $.ajax({
            type: 'POST',
            url: 'http://tsg-vending.herokuapp.com/money/' + currentMoney + '/item/' + currentItemId,
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (data, status) {
                numDollars = 0;
                numQuarters = data.quarters;
                numDimes = data.dimes;
                numNickels = data.nickels;
                numPennies = data.pennies;


                $('#message-text').val( "Thank you!!!");
                loadItems();
                refreshMoney();
            },
            error: function (xhr, status, error) {
                var responseText = jQuery.parseJSON(xhr.responseText);
                var errorMessage = responseText.message;
                if(errorMessage.match(/undefined/)) {
                    errorMessage = "Messages";
                }

                $('#message-text').val(errorMessage);
            }

        });
    });
    $('#change-return').click(function(event) {
        var stringChange = changeInString(numQuarters, numDimes, numNickels, numPennies);
        $('#change-text').val(stringChange);
        numDollars = 0;
        numQuarters = 0;
        numDimes = 0;
        numPennies = 0;
        numNickels = 0;
        setTimeout(completePurchase, 1);
    });
    $('.input-clearer').click(function(event) {
        if (purchaseComplete) {
            setTimeout(clearInput);
        }
    });
    $('#dollar').click(function(event) {
        numDollars ++;
        refreshMoney();
    });
    $('#quarter').click(function(event) {
        numQuarters ++;
        refreshMoney();
    });
    $('#dime').click(function(event) {
        numDimes ++;
        refreshMoney();
    });
    $('#nickel').click(function(event) {
        numNickels ++;
        refreshMoney();
    });
    $('#change-return').click(function (event) {
        if (numQuarters > 0 || numDollars > 0) {

        }
    })

});

$(function(){
    $('.selectpicker').selectpicker();
});
function loadItems() {

    let itemSlot = 1;
    $.ajax({
            type: 'GET',
            url: ' http://tsg-vending.herokuapp.com/items',
            success: function (itemArray) {
                $.each(itemArray, function (index, item) {
                    let id = item.id;
                    let name = item.name;
                    let price = item.price;
                    let quantity = item.quantity;

                    $('#number-' + itemSlot).text(id);
                    $('#item-name' + itemSlot).text(name);
                    $('#item-price' + itemSlot).text("$" + price.toFixed(2));
                    $('#item-quantity' + itemSlot).text(quantity);
                    global.slotItemMap.set(itemSlot, id);
                    itemSlot++;
                    totalSlotsLoaded++;
                })
               if (totalSlotsLoaded < 12 && totalSlotsLoaded >= 9) {
                   let i;
                   for (i=12; i>totalSlotsLoaded; i--){
                       $('#slot' + i).css('background-color', '#808782');
                       $('#slot' + i).css('color', '#808782');
                    }
               }
                if (totalSlotsLoaded <= 9) {
                    let i;
                    for (i=12; i>9; i--) {
                        $('#slot' + i).hide();
                    }
                }
            },
            error: function () {
                $('#errorMessages')
                    .append($('<li>')
                        .attr({class: 'list-group-item list-group-item-danger'})
                        .text('Error calling web service.  Please try again later.'));
            }
        }
    );

}



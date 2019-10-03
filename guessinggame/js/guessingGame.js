var currentGame;
$(document).ready(function () {

    loadGames();
    $('#add-guess').click(function(event ){
        //var haveValidationErrors = checkAndDisplayValidationErrors($('#add-guess').find('input'));

        // if we have errors, bail out by returning false
        //if (haveValidationErrors) {
          //  return false;
        //}
        let guessValidate = $('#guess-input').val();
        let regex = /^[0-9]{4}$/;
        if (!guessValidate.match(regex)) {
            alert("please enter a 4 digit number!");
            return;
        }

        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/api/guess',
            data: JSON.stringify({
                gameId: currentGame,
                guess: $('#guess-input').val(),
            }),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            'dataType': 'json',
            success: function(data, status) {
                let gameId = $('#game-input').val();
                showRounds(currentGame);
                $('#guess-input').val('');
            },
            error: function() {
                $('#errorMessages')
                    .append($('<li>')
                        .attr({class: 'list-group-item list-group-item-danger'})
                        .text('Error calling web service.  Please try again later.'));
            }
        });
    });
    $('#guess-for-me').click(function(event ) {
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/api/guessforme',
            data: JSON.stringify({
                gameId: currentGame
            }),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            'dataType': 'json',
            success: function (data, status) {
                let gameId = $('#game-input').val();
                showRounds(currentGame);
                $('#guess-input').val('');
            },
            error: function () {
                $('#errorMessages')
                    .append($('<li>')
                        .attr({class: 'list-group-item list-group-item-danger'})
                        .text('Error calling web service.  Please try again later.'));
            }
        });
    });
        $('#play-for-me').click(function(event ) {
            $.ajax({
                type: 'POST',
                url: 'http://localhost:8080/api/playforme',
                data: JSON.stringify({
                    gameId: currentGame
                }),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                'dataType': 'json',
                success: function (data, status) {
                    let gameId = $('#game-input').val();
                    showRounds(currentGame);
                    $('#guess-input').val('');
                    $('#errorMessages').empty();
                    loadGames();
                },
                error: function () {
                    $('#errorMessages')
                        .append($('<li>')
                            .attr({class: 'list-group-item list-group-item-danger'})
                            .text('Error calling web service.  Please try again later.'));
                }
            });
        });

    $('#new-game').click(function (event) {
        // if we made it here, there are no errors so make the ajax call
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/api/begin',


            success: function(data, status) {
                // clear errorMessages
                $('#errorMessages').empty();

                loadGames();
            },
            error: function() {
                $('#errorMessages')
                    .append($('<li>')
                        .attr({class: 'list-group-item list-group-item-danger'})
                        .text('Error calling web service.  Please try again later.'));
            }
        });
    });

});
function showRounds(gameId) {
    // clear errorMessages
    $('#errorMessages').empty();

    var gameHeader = $('#game-header');
    var roundRows = $('#round-rows');
    $('#round-rows').empty();
    // get the contact details from the server and then fill and show the
    // form on success
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/api/rounds/' + gameId,
        success: function (roundArray) {
            $.each(roundArray, function(index, round){
                var roundId = round.roundId;
                var guess = round.guess;
                var result = round.result;
                var timestamp = round.time;

                var row = '<tr>';
                row += '<td>' + roundId + '</td>';
                row += '<td>' + guess + '</td>';
                row += '<td>' + result + '</td>';
                row += '<td>' + timestamp + '<td>';

                roundRows.append(row);
            })
            $('#current-game').empty();
            $('#current-game').append(gameId);

        },
        error: function() {
            $('#errorMessages')
                .append($('<li>')
                    .attr({class: 'list-group-item list-group-item-danger'})
                    .text('Error calling web service.  Please try again later.'));
        }
    });
    currentGame = gameId;

}

function loadGames() {
    var contentRows = $('#contentRows');

    $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/api/game',
            success: function (gameArray) {
                $.each(gameArray, function (index, game) {
                    var gameId = game.gameId;
                    var answer = game.answer;

                    var solvedB = game.solved;
                    if (solvedB) {
                        solved = "Complete";
                    } else {
                        solved = "In Progress";
                    }

                    var row = '<tr>';
                    row += '<td>' + gameId + '</td>';
                    row += '<td>' + answer + '</td>';
                    row += '<td>' + solved + '</td>';
                    row += '<td><a onclick="showRounds(' + gameId + ')" type="button" class="btn btn-info btn-xs">Show Rounds</a><td>';

                    contentRows.append(row);
                })
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



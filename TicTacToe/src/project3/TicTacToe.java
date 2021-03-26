package project3;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static project3.Symbol.*;
import static java.lang.Thread.sleep;

public class TicTacToe {
    public static void main(String[] args) throws IOException {
        while (true) {
            // get my OPEN games - for now assume there is only one game
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            Request request = new Request.Builder()
                    .url("https://www.notexponential.com/aip2pgaming/api/index.php?type=myOpenGames")
                    .method("GET", null)
                    .addHeader("x-api-key", "998bc59f37ed76e14f08")
                    .addHeader("userid", "1060")
                    .build();
            Response response;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            // Determine if you are X or 0. if your id is second in the game data then X else O
            String jsonData = response.body().string();
            JSONObject Jobject = new JSONObject(jsonData);
            JSONArray games = Jobject.getJSONArray("myGames");
            for (int i = 0; i < games.length(); i++) {

                String gameId = games.getJSONObject(i).keys().next();
                String[] teams = games.getJSONObject(i).getString(gameId).split(":");

                MiniMax.mySymbol = X;
                MiniMax.opSymbol = O;

                if (teams[0].equals("1258")) {
                    // We are playing with O
                    MiniMax.mySymbol = O;
                    MiniMax.opSymbol = X;

                }

                // determine if it is my move
                OkHttpClient clientGetMove = new OkHttpClient().newBuilder()
                        .build();
                Request requestGetMove = new Request.Builder()
                        .url("https://www.notexponential.com/aip2pgaming/api/index.php?type=moves&gameId=" + gameId + "&count=1")
                        .method("GET", null)
                        .addHeader("x-api-key", "998bc59f37ed76e14f08")
                        .addHeader("userid", "1060")
                        .build();
                Response responseGetMove = clientGetMove.newCall(requestGetMove).execute();
                String responseGetMoveString = responseGetMove.body().string();
                JSONObject responseGetMoveJSON = new JSONObject(responseGetMoveString);
                if (!responseGetMoveJSON.getString("code").equals("OK")) {
                    if (!responseGetMoveJSON.getString("message").equals("No moves")) {
                        System.out.println("Not my move");
                        sleepSec(5);
                        continue;
                    }
                } else {
                    if (responseGetMoveJSON.getJSONArray("moves").isEmpty() && MiniMax.mySymbol != O) { // means op has to start the game
                        System.out.println("Not my move");
                        sleepSec(5);
                        continue;
                    } else if (responseGetMoveJSON.getJSONArray("moves").getJSONObject(0).getString("teamId").equals("1258")) { // not my move
                        System.out.println("Not my move");
                        sleepSec(5);
                        continue;
                    } else {
                        System.out.println("My move");
                    }
                }


                // Build board
                OkHttpClient clientBoard = new OkHttpClient().newBuilder()
                        .build();
                Request gameBoardRequest = new Request.Builder()
                        .url("https://www.notexponential.com/aip2pgaming/api/index.php?type=boardString&gameId=" + gameId)
                        .method("GET", null)
                        .addHeader("x-api-key", "998bc59f37ed76e14f08")
                        .addHeader("userid", "1060")
                        .build();
                Response gameBoardResponse = clientBoard.newCall(gameBoardRequest).execute();
                String jsonBoardData = gameBoardResponse.body().string();
                JSONObject boardObject = new JSONObject(jsonBoardData);
                String boardString = boardObject.getString("output");
                Board board = Board.createFromString(boardString, boardObject.getInt("target"));

                if (!board.anyMovesRemain()) {
                    System.out.println("Game is over");
                    sleepSec(5);
                    continue;
                }
                // Make a move
                Move move = MiniMax.getNextMove(board);

                OkHttpClient clientMove = new OkHttpClient().newBuilder()
                        .build();
                RequestBody bodyMove = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("type", "move")
                        .addFormDataPart("teamId", "1258")
                        .addFormDataPart("gameId", gameId)
                        .addFormDataPart("move", move.x + "," + move.y)
                        .build();
                Request requestMove = new Request.Builder()
                        .url("https://www.notexponential.com/aip2pgaming/api/index.php")
                        .method("POST", bodyMove)
                        .addHeader("x-api-key", "998bc59f37ed76e14f08")
                        .addHeader("userid", "1060")
                        .build();
                Response responseMove = clientMove.newCall(requestMove).execute();
                // check if ok or fail
                String responseMoveString = responseMove.body().string();
                JSONObject responseMoveJSON = new JSONObject(responseMoveString);
                String responseMoveCode = responseMoveJSON.getString("code");
                System.out.println("Make move code: " + responseMoveCode);
                sleepSec(5);
            }
        }
    }

    public static void sleepSec(int second) {
        try {
            System.out.println("start sleeping");
            sleep(second * 1000);
            System.out.println("stop sleeping");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

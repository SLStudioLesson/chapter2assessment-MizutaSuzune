package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import data.RecipeFileHandler;


public class RecipeUI {
    private BufferedReader reader;
    private RecipeFileHandler fileHandler;

    public RecipeUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        fileHandler = new RecipeFileHandler();
    }

    public RecipeUI(BufferedReader reader, RecipeFileHandler fileHandler) {
        this.reader = reader;
        this.fileHandler = fileHandler;
    }

    public void displayMenu() {
        while (true) {
            try {
                System.out.println();
                System.out.println("Main Menu:");
                System.out.println("1: Display Recipes");
                System.out.println("2: Add New Recipe");
                System.out.println("3: Search Recipe");
                System.out.println("4: Exit Application");
                System.out.print("Please choose an option: ");

                String choice = reader.readLine();

                switch (choice) {
                    case "1":
                        // 設問1: 一覧表示機能
                        displayRecipes();
                        break;
                    case "2":
                        // 設問2: 新規登録機能
                        addNewRecipe();
                        break;
                    case "3":
                        // 設問3: 検索機能
                        searchRecipe();
                        break;
                    case "4":
                        System.out.println("Exit the application.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select again.");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Error reading input from user: " + e.getMessage());
            }
        }
    }

    /**
     * 設問1: 一覧表示機能
     * RecipeFileHandlerから読み込んだレシピデータを整形してコンソールに表示します。
     */
    private void displayRecipes() {
        // readRecipesメソッドから受け取ったリストの一つ目の","までをレシピ名・以降を材料として出力
        System.out.println("Recipes:");
        for(String disp :fileHandler.readRecipes()) {
            System.out.println("-----------------------------------");
            System.out.println("Recipe Name: " + disp.substring(0,disp.indexOf(",")));
            System.out.println("Main Ingredients: " + disp.substring(disp.indexOf(",") + 1));
            System.out.println("-----------------------------------");
        }
    }

    /**
     * 設問2: 新規登録機能
     * ユーザーからレシピ名と主な材料を入力させ、RecipeFileHandlerを使用してrecipes.txtに新しいレシピを追加します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void addNewRecipe() throws IOException {
        // 入力内容をaddRecipeメソッドに引数として渡す
        System.out.print("Enter recipe name: ");
        String recipeNm = reader.readLine();
        System.out.print("Enter main ingredients: ");
        String ingredients = reader.readLine();
        fileHandler.addRecipe(recipeNm,ingredients);
        //reader.close();

    }

    /**
     * 設問3: 検索機能
     * ユーザーから検索クエリを入力させ、そのクエリに基づいてレシピを検索し、一致するレシピをコンソールに表示します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void searchRecipe() throws IOException {
        // 入力値を配列に設定し、レシピ名・材料名のフィールドに格納
        System.out.print("Enter search query  (e.g., 'name=Tomato&ingredient=Garlic'): ");
        String[] searchWord = (reader.readLine()).split("&");
        String searchNm = null;
        String searchIng = null;
        for (String word : searchWord) {
            if (word.split("=")[0].equals( "name")) {
                searchNm = word.split("=")[1];
            } else if (word.split("=")[0].equals("ingredient")) {
                searchIng = word.split("=")[1];
            } else {
                System.out.println("入力内容のフォーマットを確認してください");
            }
        }
        // ファイルの内容をreadRecipesメソッドを使用し、配列で受け取る
        // 受け取った配列を1つずつ","で分割し、配列に変換
        // 変換した配列の[0]にレシピ名、以降に[材料]が設定されている前提で一致するかどうか判定
        System.out.println("Search Result:");
        boolean flag = true;
        for (String all : fileHandler.readRecipes()) {
            String[] findArray = all.split(",");
            for (int k = 0; k < findArray.length; k++) {
                // nameの場合は配列[0]で検索ワードが含まれれば該当
                    if ((findArray[0].indexOf(searchNm)) != -1) {
                        System.out.println(all);
                        break;
                    } else if ((findArray[k].indexOf(searchIng)) != -1) {
                        System.out.println(all);
                        break;
                    } else {
                        flag = false;
                    }
            }
        }
        if (flag == false) {
            System.out.println("No recipes found matching the criteria.");
        }
    }
}



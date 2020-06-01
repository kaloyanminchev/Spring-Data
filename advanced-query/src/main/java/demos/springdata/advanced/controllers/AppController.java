package demos.springdata.advanced.controllers;

import demos.springdata.advanced.entities.Ingredient;
import demos.springdata.advanced.entities.Label;
import demos.springdata.advanced.entities.Size;
import demos.springdata.advanced.services.IngredientService;
import demos.springdata.advanced.services.LabelService;
import demos.springdata.advanced.services.ShampooService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AppController implements CommandLineRunner {
    private final ShampooService shampooService;
    private final LabelService labelService;
    private final IngredientService ingredientService;
    private BufferedReader reader;

    @Autowired
    public AppController(ShampooService shampooService, LabelService labelService, IngredientService ingredientService) {
        this.shampooService = shampooService;
        this.labelService = labelService;
        this.ingredientService = ingredientService;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Enter task number:");
        int taskNumber = Integer.parseInt(reader.readLine());

        switch (taskNumber) {
            case 1:
                System.out.println("Enter shampoo size:");
                String shampooSize = reader.readLine().toUpperCase();
                Size size = Size.valueOf(shampooSize);

                this.shampooService.getAllShampoosBySize(size)
                        .forEach(s -> System.out.printf("%s %s %.2flv.%n",
                                s.getBrand(),
                                s.getSize(),
                                s.getPrice())
                        );
                break;
            case 2:
                System.out.println("Enter shampoo size and label id:");
                shampooSize = reader.readLine().toUpperCase();
                Label label = this.labelService.findById(Long.parseLong(reader.readLine()));

                size = Size.valueOf(shampooSize);
                this.shampooService.getAllShampoosBySizeOrLabel(size, label)
//                        .stream()
//                        .sorted(Comparator.comparing(Shampoo::getPrice))
                        .forEach(s -> System.out.printf("%s %s %.2flv.%n",
                                s.getBrand(),
                                s.getSize(),
                                s.getPrice())
                        );
                break;
            case 3:
                System.out.println("Enter price:");
                BigDecimal price = new BigDecimal(reader.readLine());
                this.shampooService.getShampoosWithPriceHigherThan(price)
//                        .stream()
//                        .sorted((s1, s2) -> s2.getPrice().compareTo(s1.getPrice()))
                        .forEach(s -> System.out.printf("%s %s %.2flv.%n",
                                s.getBrand(),
                                s.getSize(),
                                s.getPrice())
                        );
                break;
            case 4:
                System.out.println("Enter starting letters:");
                String startsWith = reader.readLine();
                this.ingredientService.findAllByNameStartsWith(startsWith)
                        .forEach(i -> System.out.println(i.getName()));
                break;
            case 7:
                List<Ingredient> ingredients = new ArrayList<>();
                System.out.println("Enter ingredients:");
                String ingredientName;
                while (!"".equals(ingredientName = reader.readLine())) {
                    ingredients.add(this.ingredientService.getIngredientByName(ingredientName));
                }

                this.shampooService.findShampoosWithIngredients(ingredients)
                        .forEach(s -> System.out.printf("%s %s%n",
                                s.getBrand(),
                                s.getIngredients()
                                        .stream()
                                        .map(Ingredient::getName)
                                        .collect(Collectors.toList()))
                        );
                break;
            case 8:
                System.out.println("Enter count of ingredients:");
                int count = Integer.parseInt(reader.readLine());

                this.shampooService.getShampoosByCountOfIngredientsLessThan(count)
                        .forEach(s -> System.out.printf("%s%n", s.getBrand()));
                break;
            case 10:
                System.out.println("Updated records: " +
                        this.ingredientService.updateIngredientsPrice());
                break;
        }
    }
}

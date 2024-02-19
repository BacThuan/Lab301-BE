package com.application.backend.initData;

import com.application.backend.dao.*;
import com.application.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;


@Component
public class InitData implements CommandLineRunner {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private StateDao stateDao;

    @Autowired
    private AuthProviderDao authProviderDao;
    @Autowired
    private ColorDao colorDao;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductItemDao productItemDao;
    @Autowired
    private ProductImageDao productImageDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private ProductReviewDao productReviewDao;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        addRole();
        addState();
        addAuthProvider();
        addColor();
        addBrand();
        addCategory();
        addAdminAccount();
//        addDummyUser();
//        addImage();
//        addDummyProduct();
//        addDummyProductItem();
//        addDummyOrder();
//        addDummyProductReview();
    }

    // add role default
    public void addRole(){
        Long roleTotal = roleDao.count();
        if(roleTotal == 0){
            roleDao.save((new Role("ADMIN" ,true,true,true,true)));
            roleDao.save((new Role("STAFF",false,true,false,false)));
            roleDao.save((new Role("CLIENT",false,false,false,false)));

        }
    }

    // add state default
    public void addState(){
        Long stateTotal = stateDao.count();
        if(stateTotal == 0){
            stateDao.save((new State("USER_PENDING")));
            stateDao.save((new State("USER_ENABLE")));
            stateDao.save((new State("USER_DISABLE")));

            stateDao.save((new State("PRODUCT_OUT_OF_STOCK")));
            stateDao.save((new State("PRODUCT_SELLING")));
            stateDao.save((new State("PRODUCT_STOP_SELLING")));

            stateDao.save((new State("PAYMENT_UNPAID")));
            stateDao.save((new State("PAYMENT_PAID")));
            stateDao.save((new State("PAYMENT_REFUNDED")));
            stateDao.save((new State("PAYMENT_CANCELED")));

            stateDao.save((new State("ORDER_NOT_APPROVED")));
            stateDao.save((new State("ORDER_WAITING_DELIVERY")));
            stateDao.save((new State("ORDER_DELIVERING")));
            stateDao.save((new State("ORDER_DELIVERY_FAIL")));
            stateDao.save((new State("ORDER_DELIVERY_SUCCESS")));
            stateDao.save((new State("ORDER_CANCELED")));

        }
    }

    // add provider auth default
    public void addAuthProvider(){
        Long authTotal = authProviderDao.count();
        if(authTotal == 0){
            authProviderDao.save((new AuthProvider("LOCAL")));
            authProviderDao.save((new AuthProvider("FACEBOOK")));
            authProviderDao.save((new AuthProvider("GOOGLE")));
        }
    }

    // add color default 10
    public void addColor(){
        Long colorTotal = colorDao.count();
        if(colorTotal == 0){
            colorDao.save(new Color("Black" ,"#000000"));
            colorDao.save(new Color("White" ,"#FFFFFF"));
            colorDao.save(new Color("Red" ,"#FF0000"));
            colorDao.save(new Color("Green" ,"#00FF00"));
            colorDao.save(new Color("Blue" ,"#0000FF"));
            colorDao.save(new Color("Yellow" ,"#FFFF00"));
            colorDao.save(new Color("Pink" ,"#FFC0CB"));
            colorDao.save(new Color("Orange" ,"#FFA500"));
            colorDao.save(new Color("Purple" ,"#800080"));
            colorDao.save(new Color("Gray" ,"#808080"));
        }
    }

    // add default brand 8
    public void addBrand(){
        if(brandDao.count() == 0){
            brandDao.save(new Brand("Lacoste"));
            brandDao.save(new Brand("Converse"));
            brandDao.save(new Brand("Adidas"));
            brandDao.save(new Brand("Louis Vuitton"));
            brandDao.save(new Brand("New Balance"));
            brandDao.save(new Brand("Puma"));
            brandDao.save(new Brand("Reebok"));
            brandDao.save(new Brand("Nike"));
        }
    }
    // add default category 13
    public void addCategory(){
        if(categoryDao.count() == 0){
            categoryDao.save(new Category("Sneakers"));
            categoryDao.save(new Category("Loafers"));
            categoryDao.save(new Category("Oxfords"));
            categoryDao.save(new Category("Brogues"));
            categoryDao.save(new Category("Sandals"));
            categoryDao.save(new Category("Boots"));
            categoryDao.save(new Category("Flip-flops"));
            categoryDao.save(new Category("Wedges"));
            categoryDao.save(new Category("High heels"));
            categoryDao.save(new Category("Moccasins"));
            categoryDao.save(new Category("Espadrilles"));
            categoryDao.save(new Category("Clogs"));
            categoryDao.save(new Category("Slippers"));
        }
    }

    // add admin account
    public void addAdminAccount(){
        Long userTotal = userDao.count();
        if(userTotal == 0){
            Role admin = roleDao.findByRoleName("ADMIN");
            State enable = stateDao.findByStateName("USER_ENABLE");
            AuthProvider authProvider = authProviderDao.findByProviderName("LOCAL");

            User newAdmin = new User();
            newAdmin.setEmail(System.getenv("ADMIN_EMAIL"));
            newAdmin.setPassword(encoder.encode(System.getenv("ADMIN_PASSWORD")));
            newAdmin.setName(System.getenv("ADMIN_NAME"));
            newAdmin.setPhone(System.getenv("ADMIN_PHONE"));

            newAdmin.setRole(admin);
            newAdmin.setState(enable);
            newAdmin.addAuthProvider(authProvider);

            userDao.save(newAdmin);
        }
    }



    public void addUser(String email, String name, String phone, Integer diffTime,Role role, State state, AuthProvider auth){
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, - diffTime);

        Date datePrevious = calendar.getTime();
        User user;

        if(auth.getProviderName().equals("LOCAL")) user = new User(email,encoder.encode("123456789"), name,phone,role,state);
        else user = new User(email,"---------", name,phone,role,state);

        user.setLoginDate(datePrevious);
        user.addAuthProvider(auth);
        userDao.save(user);

    }


    // add dummy user
    public void addDummyUser(){
        Role admin = roleDao.findByRoleName("ADMIN");
        Role staff = roleDao.findByRoleName("STAFF");
        Role client = roleDao.findByRoleName("CLIENT");

        State pending = stateDao.findByStateName("USER_PENDING");
        State enable = stateDao.findByStateName("USER_ENABLE");
        State disable = stateDao.findByStateName("USER_DISABLE");


        AuthProvider local = authProviderDao.findByProviderName("LOCAL");
        AuthProvider facebook = authProviderDao.findByProviderName("FACEBOOK");
        AuthProvider google = authProviderDao.findByProviderName("GOOGLE");

        addUser("johnsmith@gmail.com","John Smith","0939483938",3*7,staff,enable,local);
        addUser("johnsnow@gmail.com","John Snow","0939483234",30,client,enable,local);
        addUser("jamesborn@gmail.com","James Born","0939483234",3*30,staff,enable,facebook);
        addUser("maryjane@gmail.com", "Mary Jane","0938693938",365,staff,enable,google);
        addUser("michaelsmith@gmail.com","Michael Smith","0153483938",5*7,client,disable,local);

        addUser("davidvilla@gmail.com","David Villa","0939049938",3*30,client,disable,local);
        addUser("williamberkin@gmail.com","William Berkin","0939483938",3,client,pending,local);
        addUser("amandawaller@gmail.com","Amanda Waller","0936983938",15,client,enable,local);
        addUser("jasonsmith@gmail.com","Jason Smith","0939445238",3*3,client,enable,local);
        addUser("johngary@gmail.com","John Gary","0939049938",3*30,client,disable,local);

        addUser("davidjame@gmail.com","David Jame","0863483938",2*365,client,disable,facebook);
        addUser("nicholascage@gmail.com","Nicholas Cage","0989383938",3*30,client,enable,local);
        addUser("patrickbateman@gmail.com","Patrick Bateman","0933543938",7*7,client,disable,google);
        addUser("frankmoore@gmail.com","Frank Moore","0936743938",3*3,client,pending,local);
        addUser("jackbateman@gmail.com","Jack Bateman","0940943938",8*7,client,enable,facebook);

        addUser("tylerone@gmail.com","Tyler One","0933540578",3*10,client,enable,local);
        addUser("aaronsmith@gmail.com","Aaron Smith","0933394938",10*7,client,enable,local);
        addUser("adamwarlock@gmail.com","Adam Warlock","0933573638",10*30,client,enable,google);

    }

    // add image
    public void addAnImage (String name) throws IOException {
        ProductImage productImage = new ProductImage();
        productImage.setName(name);
        productImage.setUrl(System.getenv("API_SERVER")+"/images/" + name);
        productImageDao.save(productImage);
    }
    // add some image
    public void addImage() throws IOException {
        if(productImageDao.count() == 0){
            for(int i = 1; i < 100; ++ i){
                String name = "dummy" + i +".jpg";
                addAnImage(name);
            }
        }
    }

    public String shortDesc = ".Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
            "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
            "Id cursus metus aliquam eleifend. Mollis nunc sed id semper risus. Natoque penatibus et magnis dis parturient montes. Elementum facilisis leo vel fringilla est ullamcorper.";
    public String longDesc= "\n- Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
            "\n- Etiam dignissim diam quis enim lobortis scelerisque fermentum dui faucibus. Congue nisi vitae suscipit tellus." +
            "\n- Iaculis urna id volutpat lacus laoreet. Habitant morbi tristique senectus et. Enim diam vulputate ut pharetra sit amet aliquam id diam." +
            "\n- Sit amet justo donec enim diam vulputate ut pharetra. Metus aliquam eleifend mi in." +
            "\n- In ornare quam viverra orci sagittis. Nec dui nunc mattis enim ut tellus elementum." +
            "\n- Ut lectus arcu bibendum at varius vel. Viverra adipiscing at in tellus integer feugiat scelerisque.";

    // random int
    public long getRandom(String type){
        Random random = new Random();
        int min = 1;
        int max = 100;

        if(type.equals("product_state")) {
            min = 4;
            max = 6;
        }
        else if(type.equals("payment_state")) {
            min = 7;
            max = 10;
        }
        else if(type.equals("order_state")) {
            min = 11;
            max = 16;
        }
        else if(type.equals("color")) {
            max = 10;
        }
        else if(type.equals("brand")) {
            max = 8;
        }
        else if(type.equals("category")) {
            max = 13;
        }
        else if(type.equals("product")) {
            max = 16;
        }
        else if(type.equals("user")) {
            max = 18;
        }

        return random.nextLong(max - min + 1) + min;
    }

    public int getRandom(int min, int max){
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    // random long
    public long getRandomPrice(){
        Random random = new Random();
        // Đặt khoảng giới hạn cho số ngẫu nhiên
        int minValue = 10000;
        int maxValue = 50000000; // Đặt giá trị tối đa sao cho nó là bội số của 10000

        // Lấy số ngẫu nhiên chia hết cho 10,000 trong khoảng giới hạn
        return random.nextInt((maxValue / 10000) - (minValue / 10000) + 1) * 10000 + minValue;

    }

    // create product
    public void createProduct(Integer number){
        List<String> genders = new ArrayList<>(Arrays.asList("Male", "Female"));

        Product product = new Product();

        product.setName("Shoe "+ number);
        product.setGender(genders.get(getRandom(0,1)));
        product.setShortDesc("This is shoe " + number + shortDesc);
        product.setLongDesc("With a very large content "+ number + " "+ longDesc);

        product.setBrand(brandDao.findById(getRandom("brand")));
        product.setCategory(categoryDao.findById(getRandom("category")));

        productDao.save(product);

    }
    // add dummy product
    public void addDummyProduct(){
        for(int i = 0; i < 16 ; ++ i){
            createProduct(i + 1);
        }
    }

    // create product Item
    public void createProductItem(int i){
        ProductItem productItem = new ProductItem();

        List<ProductImage> listImages = new ArrayList<>();
        Product product = productDao.findById(getRandom("product"));
        for(long number :  new ArrayList<>(Arrays.asList(i, i - 1, i - 2, i - 3, i - 4))){
            listImages.add(productImageDao.findById(number));
        }

        productItem.setQuantity(getRandom(1,100));
        productItem.setPrice(getRandomPrice());
        productItem.setSize(getRandom(37,50));


        productItem.setPrimaryImage(listImages.get(0).getUrl());

        productItem.setColor(colorDao.findById(getRandom("color")));
        productItem.setProduct(product);
        productItem.setState(stateDao.findById(getRandom("product_state")));

        for(ProductImage image : listImages){
            productItem.addImage(image);
        }

        ProductItem result = productItemDao.save(productItem);

        for(ProductImage image : listImages){
            image.setProductItem(result);
        }

        product.addItem(result);

        productImageDao.saveAll(listImages);
        productDao.save(product);
    }
    public void addDummyProductItem() {

        for(int i = 1; i < 21; ++ i){
            createProductItem(i * 5);
        }
    }

    // create order
    public void createOrder (int year, int month){
        User user = userDao.findById(getRandom("user"));
        State paymentState = stateDao.findById(getRandom("payment_state"));
        State orderState = stateDao.findById(getRandom("order_state"));
        List<String> methods = new ArrayList<>(Arrays.asList("Pay when received", "Credit card"));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, getRandom(1,28));

        // create order
        Order order = new Order();
        order.setEmail(user.getEmail());
        order.setAddress("Lorem ipsum dolor sit amet, consectetur adipiscing elit");
        order.setName(user.getName());
        order.setPhone(user.getPhone());
        order.setTotal(getRandomPrice());
        order.setMethod(methods.get(getRandom(0,1)));
        order.setCreatedAt(calendar.getTime());
        order.setUpdatedAt(calendar.getTime());

        order.setPaymentState(paymentState);
        order.setDeliveryState(orderState);
        order.setUser(user);

        Order savedOrder = orderDao.save(order);
        Random random = new Random();
        // create order detail
        for(int i = 0; i < 5 ; ++ i){
            OrderDetail orderDetail = new OrderDetail();

            ProductItem item = productItemDao.findById(1 + random.nextInt(18));

            orderDetail.setQuantity(getRandom(1,50));
            orderDetail.setOrder(savedOrder);
            orderDetail.setProductItem(item);

            orderDetailDao.save(orderDetail);
        }

    }
    // add order
    public void addDummyOrder() {

        for(int i = 1; i < 13 ; ++i){
            for(int j = 0; j < getRandom(10,20); ++ j){
                createOrder(2022,i);
            }
        }

        for(int i = 1; i < 13 ; ++i){
            for(int j = 0; j < getRandom(10,20); ++ j){
                createOrder(2023,i);
            }
        }


        for(int j = 0; j < getRandom(10,20); ++ j){
            createOrder(2024,1);
        }

    }

    // create review
    public void createReview (int i){
        ProductReview productReview = new ProductReview();
        productReview.setRate(getRandom(1,5));
        productReview.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");

        Product product = productDao.findById((long) i);
        User user = userDao.findById(getRandom("user"));

        productReview.setUser(user);
        productReview.setProduct(product);

        ProductReview result = productReviewDao.save(productReview);

        product.addReview(result);
        productDao.save(product);

        user.addReview(result);
        userDao.save(user);

    }

    public void addDummyProductReview() {

        for(int i = 1; i < 17; ++ i){
            for(int j = 0; j < 10; ++ j){
                createReview(i);
            }

        }
    }

}

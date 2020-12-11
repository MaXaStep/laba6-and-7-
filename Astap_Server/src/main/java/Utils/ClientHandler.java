package Utils;

import Models.*;
import Services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Boolean isConnected;
    private RequestModel requestModel;
    private CustomerService customerService = new CustomerService();
    private OrderService orderService = new OrderService();
    private ProductService productService = new ProductService();
    private ProviderService providerService = new ProviderService();
    private OperationService operationService = new OperationService();
    private UserService userService = new UserService();
    private List<User> users;
    private List<Order> orders;
    private List<Operation> operations;
    private List<Customer> customers;
    private List<Provider> providers;
    private List<Product> products;
    private ResponseModel responseModel;
    private Gson gson;
    private BufferedReader in; // поток чтения из сокета
    private PrintWriter out; // поток записи в сокет

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        gson = new Gson();
        users = userService.findAllEntities();
        orders = orderService.findAllEntities();
        operations = operationService.findAllEntities();
        customers = customerService.findAllEntities();
        providers = providerService.findAllEntities();
        products = productService.findAllEntities();
        isConnected = true;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream());
            String word;
            while (clientSocket.isConnected()) {
                word = in.readLine();
                requestModel = gson.fromJson(word, RequestModel.class);
                if (requestModel.getRequestType() == RequestType.CreateOperation) {
                    Operation operation = gson.fromJson(requestModel.getRequestMessage(), Operation.class);
                    operation.setfUser(users.stream().filter(x -> x.getfId() == operation.getfUser().getfId()).findFirst().get());
                    operation.setfProduct(products.stream().filter(x -> x.getfId() == operation.getfProduct().getfId()).findFirst().get());
                    operation.setfOrder(orders.stream().filter(x -> x.getfId() == operation.getfOrder().getfId()).findFirst().get());
                    operationService.saveEntity(operation);
                    operations = operationService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.CreateOrder) {
                    Order order = gson.fromJson(requestModel.getRequestMessage(), Order.class);
                    order.setfCustomer(customers.stream().filter(x -> x.getfId() == order.getfCustomer().getfId()).findFirst().get());
                    order.setfProvider(providers.stream().filter(x -> x.getfId() == order.getfProvider().getfId()).findFirst().get());
                    orderService.saveEntity(order);
                    orders = orderService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.CreateProvider) {
                    Provider provider = gson.fromJson(requestModel.getRequestMessage(), Provider.class);
                    providerService.saveEntity(provider);
                    providers = providerService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.CreateProduct) {
                    Product product = gson.fromJson(requestModel.getRequestMessage(), Product.class);
                    productService.saveEntity(product);
                    products = productService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.CreateUser) {
                    User user = gson.fromJson(requestModel.getRequestMessage(), User.class);
                    if (!users.stream().anyMatch(x -> x.getfLogin().equals(user.getfLogin()))) {
                        responseModel = new ResponseModel(ResponseStatus.OK, "");
                        userService.saveEntity(user);
                        users = userService.findAllEntities();
                    } else {
                        responseModel = new ResponseModel(ResponseStatus.Error, "Пользователь с таким логином уже существует");
                    }
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.CreateCustomer) {
                    Customer customer = gson.fromJson(requestModel.getRequestMessage(), Customer.class);
                    if (customer.getfUser() != null)
                        customer.setfUser(users.stream().filter(x -> x.getfLogin().equals(customer.getfUser().getfLogin())).findFirst().get());
                    customerService.saveEntity(customer);
                    customers = customerService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.GetCustomer) {
                    Customer customer = gson.fromJson(requestModel.getRequestMessage(), Customer.class);
                    customer = customerService.findEntity(customer.getfId());
                    customer.setfUser(null);
                    customer.setfOrders(null);
                    responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(customer));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.GetProduct) {
                    Product product = gson.fromJson(requestModel.getRequestMessage(), Product.class);
                    product = productService.findEntity(product.getfId());
                    product.setfOperations(null);
                    responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(product));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.GetProvider) {
                    Provider provider = gson.fromJson(requestModel.getRequestMessage(), Provider.class);
                    provider = providerService.findEntity(provider.getfId());
                    provider.setfOrders(new HashSet<>());
                    responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(provider));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.GetOrder) {
                    Order order = gson.fromJson(requestModel.getRequestMessage(), Order.class);
                    order = orderService.findEntity(order.getfId());
                    Provider provider = order.getfProvider();
                    provider.setfOrders(new HashSet<>());
                    order.setfProvider(provider);
                    Customer customer = order.getfCustomer();
                    customer.setfOrders(new HashSet<>());
                    customer.setfUser(null);
                    order.setfCustomer(customer);
                    order.setfOperation(new HashSet<>());
                    responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(order));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.GetUser) {
                    User user = gson.fromJson(requestModel.getRequestMessage(), User.class);
                    user = userService.findEntity(user.getfId());
                    Customer customer = user.getfCustomer();
                    if (customer != null) {
                        customer.setfOrders(null);
                        customer.setfUser(null);
                    }
                    user.setfOperations(new HashSet<>());
                    responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(user));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.DeleteCustomer) {
                    Customer customer = gson.fromJson(requestModel.getRequestMessage(), Customer.class);
                    customerService.deleteEntity(customer);
                    customers = customerService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.DeleteOrder) {
                    Order order = gson.fromJson(requestModel.getRequestMessage(), Order.class);
                    order = orderService.findEntity(order.getfId());
                    Set<Operation> operations = order.getfOperation();
                    for (Operation operation :
                            operations) {
                        operationService.deleteEntity(operation);
                    }
                    orderService.deleteEntity(order);
                    orders = orderService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.DeleteProduct) {
                    Product product = gson.fromJson(requestModel.getRequestMessage(), Product.class);
                    product = productService.findEntity(product.getfId());
                    Set<Operation> operations = product.getfOperations();
                    for (Operation operation :
                            operations) {

                        operationService.deleteEntity(operation);
                    }
                    productService.deleteEntity(product);
                    products = productService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.DeleteProvider) {
                    Provider provider = gson.fromJson(requestModel.getRequestMessage(), Provider.class);
                    providerService.deleteEntity(provider);
                    providers = providerService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.DeleteUser) {
                    User user = gson.fromJson(requestModel.getRequestMessage(), User.class);
                    Customer customer = userService.findEntity(user.getfId()).getfCustomer();
                    if (customer != null) {
                        customer.setfUser(null);
                        customerService.updateEntity(customer);
                        customers = customerService.findAllEntities();
                    }
                    userService.deleteEntity(user);
                    users = userService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.UpdateOrder) {
                    Order order = gson.fromJson(requestModel.getRequestMessage(), Order.class);
                    Order tempOrder = orderService.findEntity(order.getfId());
                    tempOrder.setfStatus(order.getfStatus());
                    orderService.updateEntity(tempOrder);
                    orders = orderService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.UpdateCustomer) {
                    Customer customer = gson.fromJson(requestModel.getRequestMessage(), Customer.class);
                    customerService.updateEntity(customer);
                    customers = customerService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.UpdateUser) {
                    User user = gson.fromJson(requestModel.getRequestMessage(), User.class);
                    responseModel = new ResponseModel(ResponseStatus.OK, "");
                    userService.updateEntity(user);
                    users = userService.findAllEntities();
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.UpdateProvider) {
                    Provider provider = gson.fromJson(requestModel.getRequestMessage(), Provider.class);
                    providerService.updateEntity(provider);
                    providers = providerService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.UpdateProduct) {
                    Product product = gson.fromJson(requestModel.getRequestMessage(), Product.class);
                    productService.updateEntity(product);
                    products = productService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.UpdateOrder) {
                    Order order = gson.fromJson(requestModel.getRequestMessage(), Order.class);
                    orderService.updateEntity(order);
                    orders = orderService.findAllEntities();
                }
                if (requestModel.getRequestType() == RequestType.Login) {
                    User requestUser = gson.fromJson(requestModel.getRequestMessage(), User.class);
                    User user = new User();
                    boolean isFound = false;
                    for (User tempUser :
                            users) {
                        if (tempUser.getfLogin().equals(requestUser.getfLogin()))
                            if (tempUser.getfPassword().equals(requestUser.getfPassword())) {
                                isFound = true;
                                user = new User(tempUser.getfLogin(), tempUser.getfPassword(), tempUser.getfUsername(), tempUser.getfRole(), tempUser.getfId());
                                responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(user));
                            }
                    }
                    if (!isFound)
                        responseModel = new ResponseModel(ResponseStatus.Error, gson.toJson(user));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.ShowCustomers) {
                    List<Customer> customers = new ArrayList<Customer>();
                    for (Customer tempCustomer :
                            this.customers) {
                        customers.add(new Customer(tempCustomer.getfName(), tempCustomer.getfAddress(), tempCustomer.getfPhoneNumber(), tempCustomer.getfEmail(), tempCustomer.getfId()));
                    }
                    responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(customers));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.ShowOperations) {
                    List<Operation> operations = new ArrayList<Operation>();
                    for (Operation tempOperation :
                            this.operations) {
                        Product product = tempOperation.getfProduct();
                        product.setfOperations(null);
                        Order order = tempOperation.getfOrder();

                        order.setfOperation(null);
                        order.setfProvider(null);
                        order.setfCustomer(null);
                        User user = tempOperation.getfUser();
                        user.setfOperations(null);
                        user.setfCustomer(null);
                        Operation operation = new Operation(tempOperation.getfDate(), tempOperation.getfProductQuantity(), tempOperation.getfType(), tempOperation.getfId());
                        operation.setfProduct(product);
                        operation.setfOrder(order);
                        operation.setfUser(user);
                        operations.add(operation);
                    }
                    responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(operations));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.ShowOrders) {
                    List<Order> orders = new ArrayList<Order>();
                    for (Order tempOrder :
                            this.orders) {
                        Order sendOrder = new Order(tempOrder.getfStatus(), tempOrder.getfId());
                        Customer customer = customerService.findEntity(tempOrder.getfCustomer().getfId());
                        Customer newCustomer = new Customer();
                        Provider newProvider = new Provider();
                        newCustomer.setfId(customer.getfId());
                        newCustomer.setfPhoneNumber(customer.getfPhoneNumber());
                        newCustomer.setfName(customer.getfName());
                        newCustomer.setfEmail(customer.getfEmail());
                        newCustomer.setfAddress(customer.getfAddress());
                        Provider provider = providerService.findEntity(tempOrder.getfProvider().getfId());
                        newProvider.setfId(provider.getfId());
                        newProvider.setfPhonenumber(provider.getfPhonenumber());
                        newProvider.setfName(provider.getfName());
                        newProvider.setfEmail(provider.getfEmail());
                        newProvider.setfAddress(provider.getfAddress());
                        sendOrder.setfProvider(newProvider);
                        sendOrder.setfCustomer(newCustomer);
                        orders.add(sendOrder);
                    }
                    responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(orders));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.ShowUsers) {
                    List<User> users = new ArrayList<User>();
                    for (User tempUser :
                            this.users) {
                        users.add(new User(tempUser.getfLogin(), tempUser.getfPassword(), tempUser.getfUsername(), tempUser.getfRole(), tempUser.getfId()));
                    }
                    responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(users));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.ShowProducts) {
                    List<Product> products = new ArrayList<Product>();
                    for (Product tempProduct :
                            this.products) {
                        products.add(new Product(tempProduct.getfName(), tempProduct.getfType(), tempProduct.getfId()));
                    }
                    responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(products));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.ShowProviders) {
                    List<Provider> providers = new ArrayList<Provider>();
                    for (Provider tempProvider :
                            this.providers) {
                        providers.add(new Provider(tempProvider.getfName(), tempProvider.getfAddress(), tempProvider.getfPhonenumber(), tempProvider.getfEmail(), tempProvider.getfId()));
                    }
                    responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(providers));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.Report) {
                    int orderId = Integer.parseInt(requestModel.getRequestMessage());
                    Order order = orderService.findEntity(orderId);
                    Provider provider = order.getfProvider();
                    provider.setfOrders(new HashSet<>());
                    order.setfProvider(provider);
                    Customer customer = order.getfCustomer();
                    customer.setfOrders(new HashSet<>());
                    customer.setfUser(null);
                    order.setfCustomer(customer);
                    Set<Operation> operations = new HashSet<>();
                    for (Operation operation :
                            order.getfOperation()) {
                        Product product = operation.getfProduct();
                        product.setfOperations(null);
//                        Order tempOrder = operation.getfOrder();

//                        tempOrder.setfOperation(null);
//                        tempOrder.setfProvider(null);
//                        tempOrder.setfCustomer(null);
                        User user = operation.getfUser();
                        user.setfOperations(null);
                        user.setfCustomer(null);
                        Operation tempOperation = new Operation(operation.getfDate(), operation.getfProductQuantity(), operation.getfType(), operation.getfId());
                        tempOperation.setfProduct(product);
                        tempOperation.setfOrder(null);
                        tempOperation.setfUser(user);
                        operations.add(tempOperation);
                    }
                    order.setfOperation((Set<Operation>) operations);
                    responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(order));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.OrderReport) {
                    List<Order> resultOrders = new ArrayList<Order>();
                    User user = userService.findEntity(Integer.parseInt(requestModel.getRequestMessage()));
                    int customerId = user.getfCustomer().getfId();
                    List<Order> orders = orderService.findAllEntities().stream().filter(x -> x.getfCustomer().getfId() == customerId).collect(Collectors.toList());
                    for (Order tempOrder :
                            orders) {
                        Order sendOrder = new Order(tempOrder.getfStatus(), tempOrder.getfId());
                        Customer customer = customerService.findEntity(tempOrder.getfCustomer().getfId());
                        Customer newCustomer = new Customer();
                        Provider newProvider = new Provider();
                        newCustomer.setfId(customer.getfId());
                        newCustomer.setfPhoneNumber(customer.getfPhoneNumber());
                        newCustomer.setfName(customer.getfName());
                        newCustomer.setfEmail(customer.getfEmail());
                        newCustomer.setfAddress(customer.getfAddress());
                        Provider provider = providerService.findEntity(tempOrder.getfProvider().getfId());
                        newProvider.setfId(provider.getfId());
                        newProvider.setfPhonenumber(provider.getfPhonenumber());
                        newProvider.setfName(provider.getfName());
                        newProvider.setfEmail(provider.getfEmail());
                        newProvider.setfAddress(provider.getfAddress());
                        sendOrder.setfProvider(newProvider);
                        sendOrder.setfCustomer(newCustomer);
                        resultOrders.add(sendOrder);
                    }
                    responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(resultOrders));
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
                if (requestModel.getRequestType() == RequestType.SignUp) {
                    RegisterModel registerModel = gson.fromJson(requestModel.getRequestMessage(), RegisterModel.class);
                    User user = new User(registerModel.getLogin(), registerModel.getPassword(), registerModel.getUsername(), "Customer");
                    Customer customer = new Customer(registerModel.getUsername(), registerModel.getAddress(), registerModel.getPhonenumber(), registerModel.getEmail(), user);
                    boolean isFound = false;
                    for (User tempUser :
                            users) {
                        if (tempUser.getfLogin().equals(registerModel.getLogin())) {
                            isFound = true;
                        }
                    }
                    if (isFound)
                        responseModel = new ResponseModel(ResponseStatus.Error, gson.toJson(user));
                    else {
//                            user = new User(tempUser.getfLogin(), tempUser.getfPassword(), tempUser.getfUsername(), tempUser.getfRole(), tempUser.getfId());
                        userService.saveEntity(user);
                        customerService.saveEntity(customer);
                        users = userService.findAllEntities();
                        User returnUser = users.get(users.size() - 1);
                        user = new User(returnUser.getfLogin(), returnUser.getfPassword(), returnUser.getfUsername(), returnUser.getfRole(), returnUser.getfId());
                        responseModel = new ResponseModel(ResponseStatus.OK, gson.toJson(user));
                    }
                    out.println(gson.toJson(responseModel));
                    out.flush();
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Клиент " + clientSocket.getInetAddress() + " был закрыт...");
            try {

                clientSocket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

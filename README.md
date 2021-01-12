# Digital Innovation: Expert class - Desenvolvimento de testes unitários para validar uma API REST de gerenciamento de estoques de cerveja

Desenvolvimento de testes unitários para validar o sistema de gerenciamento de cerveja, abordando os principais conceitos e vantagens de criar testes unitários com JUnit e Mockito. Desenvolvimento das funcionalidades através da prática do TDD.

Principais tópicos abordados:

- Apresentação conceitual sobre testes: pirâmide dos tipos de testes e a importância de cada tipo durante o ciclo de desenvolvimento;
- Foco nos testes unitários: o porque é importante o desenvolvimento destes tipos de testes;
- Principais frameworks para testes unitários em Java: [JUnit](https://junit.org/junit4/), [Mockito](https://site.mockito.org/) e [Hamcrest](http://hamcrest.org/JavaHamcrest/);
- Testagem das funcionalidades básicas: criação, listagem, consulta por nome e exclusão de cervejas;
- TDD (Test Driven Development): desenvolvimento de funcionalidades importantes: incremento e decremento do número de cervejas em estoque.

O que foi utilizado para o desenvolvimento deste projeto:

- Java 11;
- Maven 3.6.3;
- [Intellij IDEA](https://www.jetbrains.com/pt-br/idea/);
- Git e GitHub.

Para executar o projeto no terminal, utilize o comando: `$ mvn spring-boot:run`. A API estará disponível em `http://localhost:8080/api/v1/beers`.

Para executar os testes: `$ mvn clean test`.

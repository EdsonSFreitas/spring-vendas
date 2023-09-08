**Boas Práticas Adotadas Nessa API REST com Spring Framework Até o Momento:**

Neste projeto estou aplicando uma série de boas práticas para garantir a qualidade e a eficiência do código da minha API REST baseada no Spring Framework. Estou aplicando técnicas diferentes para demostrar diversas maneiras de implementar os recursos explorados e algumas permissões foram propositalmente mantidas para facilitar o acesso como a console do h2 e o swagger, mas ciente de que um projeto em produção deve seguir padrões de desenvolvimento definidos no escopo do projeto/equipe. 

**1. Estrutura de Pacotes Organizada:** Mantenho uma estrutura de pacotes bem organizada, dividindo os componentes em controladores, serviços, repositórios e modelos (DTOs/Entidades).

**2. Nomenclatura Convincente:** Utilizo nomenclaturas descritivas e significativas para classes, métodos e variáveis, tornando o código mais compreensível.

**3. DTOs para Comunicação:** Emprego DTOs para representar dados enviados e recebidos pela API, protegendo detalhes da implementação.

**4. Validações de Entrada:** Implemento validações para garantir a integridade dos dados fornecidos pelos clientes da API.

**5. Respostas HTTP Adequadas:** Utilizo os códigos de status HTTP apropriados nas respostas da API, assegurando respostas claras e adequadas.

**6. Tratamento de Exceções:** Implemento um tratamento de exceções personalizado para fornecer respostas apropriadas em caso de erros e evitar a exposição de detalhes técnicos.

**7. Paginação e Ordenação:** Implemento recursos de paginação e ordenação para simplificar a navegação pelos resultados da API e um limit de requisições.

**8. Uso Consciente do Banco de Dados:** Evito consultas complexas ou repetitivas ao banco de dados, considerando a implementação de caches para otimizar o desempenho. As credenciais do banco ficam protegidas por meio de variáveis de ambiente e uso bancos de dados diferentes conforme ambiente (prod com PostgreSQL, test ou dev com H2).

**9. Código Limpo e Legível:** Mantenho meu código limpo, sigo as convenções de estilo e asseguro que ele permaneça altamente legível. Adoto uso constante de Optional, conjuntos imutáveis ao usar métodos getters etc.

**10. Princípios SOLID:** Aplico os princípios SOLID (Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion) para manter meu código limpo e altamente modular.

**11. Boas Práticas de Segurança:** Apliquei implementações de segurança, incluindo autenticação, autorização e validação de token, para proteger a API contra ameaças. Também apliquei configuração global para o CORS, validação de complexidade de senha e validação se o username já está em uso no banco de dados.

## Implementações pendentes:

** Max Retry, Bloqueio de usuario: ** Criar limite de tentativas de autenticação e bloquear usuário apóx X tentativas malsucedidas, registrar no banco as tentativas de autenticação malsucedidas etc.

** Testes Unitários: ** Criar os testes unitários para garantir a funcionalidade correta dos componentes da API, aumentando a confiabilidade do sistema.

** Monitoramento e Logs: ** Integrar registros de logs e métricas para facilitar a identificação e resolução de problemas operacionais.

** Versionamento da API: ** Implementar uma estratégia de versionamento da API para garantir compatibilidade com clientes existentes.

** Documentação Clara: ** Criar uma documentação clara e abrangente da API, descrevendo o uso de cada recurso, parâmetros necessários e formatos de resposta.
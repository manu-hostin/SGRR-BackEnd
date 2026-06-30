# SGRR - Sistema de Gestão de Recursos e Reservas

Projeto Spring Boot referente ao trabalho de Implantação de Sistemas (CentroWEG).

## Estrutura do projeto

```
src/main/java/com/centroweg/sgrr/
├── SgrrApplication.java        -> classe principal
├── model/                      -> entidades JPA (Usuario, Sala, Equipamento, Reserva)
├── dto/                        -> records de request/response (um par para cada entidade)
├── mapper/                     -> conversão entre Entity <-> DTO (@Component)
├── repository/                 -> interfaces JpaRepository (@Repository)
├── service/                    -> regras de negócio (@Service + @AllArgsConstructor)
└── controller/                 -> endpoints REST (@RestController)
```

## Entidades

- **Usuario**: nome, email, senha, perfil (ADMINISTRADOR ou USUARIO)
- **Sala**: nome, capacidade, localização
- **Equipamento**: nome, tipo, patrimônio
- **Reserva**: usuário, sala (opcional), equipamento (opcional), data/hora início e fim, status (CONFIRMADA ou CANCELADA)

A Reserva aceita Sala OU Equipamento porque o sistema reserva tanto salas quanto notebooks/projetores, conforme descrito na introdução do trabalho.

## Como rodar

1. Tenha instalado: **JDK 21**, **Maven** e **MySQL 8.0** rodando localmente.
2. Ajuste usuário/senha do banco em `src/main/resources/application.properties` se necessário (padrão: `root`/`root`).
3. O banco `sgrr_db` é criado automaticamente (`createDatabaseIfNotExist=true`) e as tabelas são geradas pelo Hibernate (`ddl-auto=update`).
4. Execute:
```bash
mvn spring-boot:run
```
5. A API sobe em `http://localhost:8081`.

## Segurança

A senha do usuário é armazenada com hash **BCrypt** (`spring-security-crypto`), nunca em texto puro. O endpoint não exige login ainda (não há Spring Security completo configurado), mas o dado sensível já não fica exposto no banco.

## Testes automatizados

Foram adicionados testes unitários (JUnit 5 + Mockito) em `src/test/java/.../service/`:
- `ReservaServiceTest`: valida rejeição de data fim ≤ data início, rejeição de conflito de horário e o caminho feliz de uma reserva.
- `UsuarioServiceTest`: garante que a senha salva nunca é igual ao texto puro digitado.

Para rodar:
```bash
mvn test
```

## Integração com o front-end (Next.js / v0)

A API libera CORS apenas para `http://localhost:3000` (ver `config/CorsConfig.java`). Veja a seção abaixo no chat/documentação do projeto para o passo a passo de como rodar back e front juntos.


## Endpoints disponíveis

| Recurso       | Método | Rota                          |
|---------------|--------|-------------------------------|
| Usuário       | POST   | /api/usuarios                 |
| Usuário       | GET    | /api/usuarios                 |
| Usuário       | GET    | /api/usuarios/{id}            |
| Usuário       | PUT    | /api/usuarios/{id}            |
| Usuário       | DELETE | /api/usuarios/{id}            |
| Sala          | POST   | /api/salas                    |
| Sala          | GET    | /api/salas                    |
| Sala          | GET    | /api/salas/{id}                |
| Sala          | PUT    | /api/salas/{id}                |
| Sala          | DELETE | /api/salas/{id}                |
| Equipamento   | POST   | /api/equipamentos             |
| Equipamento   | GET    | /api/equipamentos             |
| Equipamento   | GET    | /api/equipamentos/{id}        |
| Equipamento   | PUT    | /api/equipamentos/{id}        |
| Equipamento   | DELETE | /api/equipamentos/{id}        |
| Reserva       | POST   | /api/reservas                 |
| Reserva       | GET    | /api/reservas                 |
| Reserva       | GET    | /api/reservas/{id}             |
| Reserva       | PATCH  | /api/reservas/{id}/cancelar    |
| Reserva       | DELETE | /api/reservas/{id}             |

## Exemplo de requisição (criar reserva)

```json
POST /api/reservas
{
  "usuarioId": 1,
  "salaId": 1,
  "equipamentoId": null,
  "dataHoraInicio": "2026-06-20T14:00:00",
  "dataHoraFim": "2026-06-20T15:00:00"
}
```

Se já existir uma reserva confirmada para a mesma sala/equipamento em horário conflitante, a API retorna erro — isso cobre o item do checklist de testes: "tentar reservar o mesmo recurso no mesmo horário (conflito)".

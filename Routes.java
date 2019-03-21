import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.telegram.TelegramConstants;
import org.apache.camel.component.telegram.TelegramParseMode;
import org.apache.camel.model.dataformat.JsonLibrary;

public class Routes extends RouteBuilder {
    public void configure() {

        // Take all messages that are sent to our BOT
        from("telegram:bots/{{token}}")
                .convertBodyTo(String.class)
                .choice()

                    .when(simple("${body.toLowerCase()} contains 'chuck'"))
                        // Let's retrieve data from the external service
                        .to("direct:call")
                        // Source is HTML encoded, so we tell the Telegram component we are sending HTML encoded data
                        .setHeader(TelegramConstants.TELEGRAM_PARSE_MODE, constant(TelegramParseMode.HTML))
                        // Send the quote back to the same chat (CamelTelegramChatId header is implicitly used)
                        .to("telegram:bots/{{token}}")
                        .log("Returned: ${body}")
                    .otherwise()
                        // Just log all other messages
                        .log("Discarded: ${body}");


        from("direct:call")
                // Let's use a circuit breaker to avoid cascading failures
                .hystrix().hystrixConfiguration().executionTimeoutInMilliseconds(1000).end()
                    // When a user writes the word 'chuck' on the chat execute the following steps
                    .to("http4://api.icndb.com/jokes/random")
                    // Here we have a random quote by Chuck Norris, let's unmarshal the JSON data
                    .unmarshal().json(JsonLibrary.Jackson)
                    // We take a specific field of the JSON data
                    .transform(simple("${body[value][joke]}"))
                .onFallback()
                    .setBody(constant("Chuck Norris will kick you shortly!"))
                .end();




    }
}
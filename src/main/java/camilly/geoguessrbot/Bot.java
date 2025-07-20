package camilly.geoguessrbot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot extends ListenerAdapter {
    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.load();
            String token = dotenv.get("DISCORD_TOKEN");
            
            JDA jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).addEventListeners(new Bot()).build();

             jda.updateCommands().addCommands(
                    Commands.slash("ajuda", "Mostra os comandos disponíveis")
            ).queue();

            jda.awaitReady();
            System.out.println("Bot online");
        } catch (InterruptedException e) {
            System.err.println("Bot interrompido durante inicialização");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o bot");
            e.printStackTrace();
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Bot pronto!");
        System.out.println("Conectado como: " + event.getJDA().getSelfUser().getName());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message msg = event.getMessage();
        String content = msg.getContentRaw();

        if (content.equalsIgnoreCase("!ping")) {
            event.getChannel().sendMessage("🏓 Pong!").queue();
        }

        if (content.equalsIgnoreCase("!pong")) {
            event.getChannel().sendMessage("Ping! 🏓").queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ajuda")) {
            String ajudaMsg = """
                Comandos disponíveis:
                - `!ping` – Teste de latência
                - `!pong` – Teste de deslatência
                - `/ajuda` – Mostra esta mensagem
                """;
            event.reply(ajudaMsg).queue();
        }
    }
}

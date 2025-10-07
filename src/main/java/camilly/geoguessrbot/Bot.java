package camilly.geoguessrbot;

import java.awt.Color;
import java.time.OffsetDateTime;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class Bot extends ListenerAdapter {
    public static void main(String[] args) {
        try {
            Dotenv dotenv = Dotenv.load();
            String token = dotenv.get("DISCORD_TOKEN");
            
            JDA jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).addEventListeners(new Bot()).build();
            jda.awaitReady();

            CommandListUpdateAction comandos = jda.updateCommands();

            comandos.addCommands(
                Commands.slash("ajuda", "Mostra os comandos disponíveis"),
                Commands.slash("culpado", "Mostra o culpado de eu pensar em fazer essa loucura"),
                Commands.slash("testeembed", "Comando para testar de forma mais aprofundada Embeds")
            );

            /* Forma menos estruturada de registrar um comando slash
            jda.updateCommands().addCommands(
                Commands.slash("ajuda", "Mostra os comandos disponíveis")
            ).queue();
            */

            comandos.queue();
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

        if (content.equalsIgnoreCase("!dificil")) {
            String nEsquecer = """
                But like flower petals that will bloom again *(Oh, why, why?; Yeah)*
                Get better day by day
                Get better day by day
                """;
            event.getChannel().sendMessage(nEsquecer).queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        /* Forma menos estruturada de tratar os recebimentos dos comandos slash
        if (event.getName().equals("ajuda")) {
            String ajudaMsg = """
                Comandos disponíveis:
                - `!ping` – Teste de latência
                - `!pong` – Teste de deslatência
                - `/ajuda` – Mostra esta mensagem
                """;
            event.reply(ajudaMsg).queue();
        }
        */
        switch (event.getName()) {
            case "ajuda" -> {
                String ajudaMsg = """
                Comandos disponíveis:
                - `!ping` – Teste de latência
                - `!pong` – Teste de deslatência
                - `/ajuda` – Mostra esta mensagem
                - `/culpado` – Mostra a fonte da minha ideia
                - `/testeembed` – Testa formatação de mensagem com Embed
                """;
                event.reply(ajudaMsg).queue();
            }
            // Comando inserido para testar questões básicas da aplicação do Embed
            case "culpado" -> {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setTitle("Culpado");
                embed.setDescription("PROCURADO!!!");
                embed.setImage("https://i.imgur.com/vEeTVnN.png");
                embed.setThumbnail("https://emojigraph.org/media/whatsapp/tiger-face_1f42f.png");
                embed.setFooter("RECOMPENSA: Uma coxinha e um guaravita");
                embed.setColor(0xFF0000);
                event.replyEmbeds(embed.build()).queue();
            }
            case "testeembed" -> {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setAuthor("Alguém");
                embed.setTitle("Título");
                embed.setTitle("Título com image", "https://lh3.googleusercontent.com/yc_xj-cbyMJg2pMUZGW2yenAQOXq0lmMyRDLoEB2zztROUleMUY2SIrWEnKpDtuKFIOTF9N-CqwqoPo=w544-h544-l90-rj");
                embed.setDescription("descrição descrição descrição");
                embed.addField("Campo 1", "um", false);
                embed.addField("Campo 2", "dois", false);
                embed.addField("Campo 3", "três", false);
                embed.addBlankField(false);
                embed.addField("Campo 1", "um", true);
                embed.addField("Campo 2", "dois", true);
                embed.addField("Campo 3", "três", true);
                embed.setFooter("footer com imagem", "https://upload.wikimedia.org/wikipedia/pt/5/53/Snoopy_Peanuts.png");
                embed.setFooter("footer");
                embed.setTimestamp(OffsetDateTime.now());
                embed.setColor(Color.MAGENTA);
                event.replyEmbeds(embed.build()).queue();
            }
            default -> {
                return;
            }
        }
    }
}
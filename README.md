# Camel K: Chuck Bot

This demo shows how easy is to create a chat bot for Telegram using Camel K.

## Prerequisites

You need:

- A Kubernetes or Openshift cluster (also a local dev cluster) with Camel K installed in the current namespace. 
You can follow the [instructions in the Camel K repo](https://github.com/apache/camel-k#installation)
to install it on a development cluster (Minishift or Minikube) or on a real cluster.
- The [kamel CLI](https://github.com/apache/camel-k/releases) client tool.
- Your preferred IDE, with support for Maven. It's needed for code-completion. If you have IntelliJ Idea, Eclipse or VS Code, 
you'll find a Camel plugin that provides better support for Camel routes.

## Creating the Bot

You need to create a Bot on Telegram. The Camel K integration code will impersonate the bot.

Just follow the instructions on the [Telegram website to create a new bot](https://core.telegram.org/bots#3-how-do-i-create-a-bot).
Usually the quickest way to do it is to contact directly another Telegram bot that is called **@botfather** from you Telegram client App.

After you create the bot, you'll be given an **authorization token** that is needed to impersonate the bot.

## Running the demo

Check that you're connected to the cluster and that Camel K is installed correctly.

```
kubectl get integrationplatform camel-k -o=jsonpath='{.status.phase}'
```

If the result of the previous command is `Ready`, you're ready to go.

You can run the integration with the following command:

```
kamel run Routes.java -p token=<the-authorization-token-of-the-bot> -d camel-hystrix --dev
```

By using the `--dev` flag, you'll see the output of the pod printed in the console and the integration
will be deleted when you press `ctrl+c` to exit. You can omit the flag if you just want to keep the integration running after you leave.

## Playing with the demo

You can open your Telegram chat client and write a message to your bot (you've to write the full name of the bot. E.g. `@mybotname` for it to appear in the list of contactable users).

You can send messages to the bot. When you write the word **"chuck"** in one of your message, you'll get a famous quote about **Chuck Norris**.

E.g.

> Chuck Norris doesn't need try-catch, exceptions are too afraid to raise.

**NOTE**: we're using a service provided by "The Internet Chuck Norris Database" and we're not responsible for the content it provides. Most of the times we get random funny sentences ;)

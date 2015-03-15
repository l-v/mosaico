# Android #

## Localização ##
Em android temos a opção de obter a localização actual do dispositivo e também a opção de desenhar mapas, ver infor em http://developer.android.com/guide/topics/location/index.html. Convém notar que o desenho de mapas parece não estar disponível em todos os dispositivos.

# Desktop #

Questiono-me se a aplicação de desktop deve ser via browser ou uma aplicação standalone mesmo.
O prof falou que devia ser em java, um servlet caso fosse uma aplicação web. Não sei muito bem o que é um servlet...

## Mostrar mapas ##

Para mostrar mapas numa app de desktop em java podemos embeber uma janela de browser que seja compativel com javascript. Principalmente no desktop é importante ter esta função visto que é mais importante deixar o user escolher a localização.

Ver exemplos em http://stackoverflow.com/questions/996954/java-api-for-google-maps-or-similar

Se fizermos num browser podemos usar o app engine ou qq coisa parecida:
  * http://code.google.com/intl/pt-PT/eclipse/docs/getting_started.html
  * http://code.google.com/p/gwt-google-apis/wiki/MapsGettingStarted
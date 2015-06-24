#!/usr/bin/ruby
# -*- coding: utf-8 -*-

require "socket"

# 127.0.0.1(localhost)の1178番へ接続
sock = TCPSocket.open("pinel.local", 1178)

begin
  # サーバに文字列を送付
  sock.write("1a // ")
  
  # 相手からの文字列を出力
  p sock.gets
ensure # 例外が発生しても実行
  # 送信が終わったらソケットを閉じる
  sock.close
end




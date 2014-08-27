require "rubygems"
require "bunny"

conn = Bunny.new
conn.start

ch = conn.create_channel
q  = ch.queue("log_queue", :auto_delete => true)
x  = ch.default_exchange

IO.read(File.expand_path("~/sort.scala")).split("\n").each do
    |i| x.publish(i, :routing_key => q.name)
end

conn.close

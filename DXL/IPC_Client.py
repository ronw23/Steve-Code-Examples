import socket
from optparse import OptionParser

# Command Line Options
parser = OptionParser()
parser.add_option("--host",
                  dest="host",
                  help="IPC server hostname or IP address",
                  metavar="SERVER",
                  default="127.0.0.1")
parser.add_option("--port",
                  dest="port",
                  help="IPC server port number",
                  metavar="PORT",
                  type="int",
                  default=5093)
parser.add_option("--shutdown",
                  dest="shutdown",
                  action="store_true",
                  help="Send shutdown signal to server",
                  default=False)

(options, args) = parser.parse_args()

# Utility functions
def connect_to_IPC_server():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((options.host, options.port))
    return s

def disconnect_from_IPC_server(s):
    s.close()

def send_to_IPC_server(s, message):
    s.send(message)

def recv_from_IPC_server(s):
    return s.recv(1024)

def shutdown_IPC_server(s):
    s.send("shutdown_")

def print_session(s):
    send_to_IPC_server(s, "Date dt = session(); return_(stringOf(dt))")
    data = recv_from_IPC_server(s)
    print "DOORS client session stared:\t%s" % data

def print_client_ver(s):
    send_to_IPC_server(s, "return_(doorsInfo(3))")
    data = recv_from_IPC_server(s)
    print "DOORS client version:\t\t%s" % data

def print_server_ver(s):
    send_to_IPC_server(s, "return_(doorsInfo(6))")
    data = recv_from_IPC_server(s)
    print "DOORS server version:\t\t%s" % data


# Main
if __name__ == "__main__":

    s = connect_to_IPC_server()

    if options.shutdown:
        shutdown_IPC_server(s)
    else:
        print_session(s)
        print_client_ver(s)
        print_server_ver(s)

    disconnect_from_IPC_server(s)
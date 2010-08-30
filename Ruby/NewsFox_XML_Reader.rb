=begin
    This script takes a folder containting a master.xml file (used by the 
    NewsFox RSS extention for FireFox) on the command line and prints a 
    bunch of information from it.
    
    I don't imagine this is very useful, but just in case...

    Copyright 2010 Stephen Sloan
    This code is free software that you can redistribute and/or modify under the
    terms of the GNU General Public License as published by the Free Software 
    Foundation, either version 2 of the License, or (at your option) any later 
    version.
=end

require 'rubygems'
gem 'nokogiri'
require 'Nokogiri'

WORKING_DIR = ARGV[0]
MASTER_XML_FILE_NAME = 'master.xml'
MASTER_XML_FILE = WORKING_DIR + '\\' + MASTER_XML_FILE_NAME

# Extend string to strip CDATA characters
class String
    CDATA_REGEX = Regexp.new('<!\[CDATA\[(.*)\]\]>')
    def strip_cdata
        md = CDATA_REGEX.match(self)
        md ? md[1] : self
    end
end

# Extend the Nokogiri reader class
class XMLReader < Nokogiri::XML::Reader
    def element?
        # This method exists in git but not in my 1.4.3.1 copy
        # True when an element is opening (as in <element>)
        node_type == Nokogiri::XML::Node::ELEMENT_NODE
    end
    def element_decel?
        # True on closing elements (as in <\element>)
        node_type == Nokogiri::XML::Node::ELEMENT_DECL
    end
    def XMLReader.each_in_file(file_name)
        if File.exists? file_name
            File.open file_name, 'r' do |f|
                reader = from_io(f)
                reader.each do |node|
                    yield node
                end
            end
        end
    end
end

# Print some stuff from the input XML file
uid = nil   # Declare in top scope
ind = 2     # Spaced to indent
XMLReader.each_in_file(MASTER_XML_FILE) do |node|
        
    # This is a helper for getting elment content
    def get_inside(node)
        node.inner_xml.strip_cdata
    end

    # Print out info as elements are encountered
    # Or store state for next section
    case node.name
    when 'url'
        puts get_inside(node)
    when 'dname'
        puts ' ' * ind + 'Name: ' + get_inside(node)[0..60]
    when 'home'
        puts ' ' * ind + 'Home: ' + get_inside(node)
    when 'lastUpdate'
        puts ' ' * ind + 'Last Update: ' + get_inside(node)
    when 'uid'
        uid = node.inner_xml
    end if node.element?

    # Print out info as the <\feed> element is closing
    if node.name == 'feed' and node.element_decel?
        child_xml_file_name = WORKING_DIR + '\\' + uid + '.xml'

        XMLReader.each_in_file(child_xml_file_name) do |child_node|
            case child_node.name
            when 'title'
                puts ' ' * ind + 'Post: ' + get_inside(child_node)[0..60]
            end if child_node.element?
        end

    end

end